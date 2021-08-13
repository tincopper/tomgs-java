/*
 * File Name:WorkServer.java
 * Author:ouyangliang2
 * Date:2017年8月9日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author:ouyangliang2
 */
public class WorkServer {
    static final Logger log = LoggerFactory.getLogger(WorkServer.class);
    
    private Configuration cfg;
    private ZooKeeper zk;
    private Subscribers subscribers;
    
    // 用于保存分配给当前worker的分组
    private Set<String> assigned = new HashSet<>();
    
    private Watcher groupWatcher = (event) -> {
        if (event.getType().equals(EventType.NodeChildrenChanged)) {
            
            String group = event.getPath().substring(event.getPath().lastIndexOf(Configuration.SEPARATOR) + 1);
            
            // 动态负载均衡时，可能会将当前worker负责的分组分配给其它worker。
            if (assigned.contains(group)) {
                try {
                    List<String> events = this.getChild(event.getPath(), WorkServer.this.groupWatcher);
                    
                    this.doJob(event.getPath(), events);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    };
    
    private void doJob(String group, List<String> events) {
        Collections.sort(events, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int i1 = WorkServer.this.parseSequence(o1);
                int i2 = WorkServer.this.parseSequence(o2);
                return i1 - i2;
            }
        });
        
        // 每次只处理一个event，否则会产生羊群效应
        for (String event : events) {
            String json = null;
            try{
                json = this.getContent(group + Configuration.SEPARATOR + event, null);
            } catch(KeeperException e){
                if (e.code().equals(KeeperException.Code.NONODE)) {
                    // 同一个worker可能并行处理一个group下的事件。
                    // 如果读不到，说明该事件已经被处理了。
                    continue;
                } else {
                    log.error(e.getMessage(), e);//TODO 这里不退出的话，事件处理顺序得不到保证
                }
            } catch(InterruptedException e){
                log.error(e.getMessage(), e);
                //TODO 这里不退出的话，事件处理顺序得不到保证
            }
            
            try{
                if (this.delZnode(group + Configuration.SEPARATOR + event)) {
                    // TODO connection loss时可能会丢失事件
                    Event e = Event.fromJson(json);
                    subscribers.getSubscribers(e.getType()).forEach((s) -> {
                        s.onEvent(e);
                    });
                    break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            
        }
    }
    
    private int parseSequence(String path) {
        return Integer.parseInt(path.substring(path.length() - 10));
    }
    
    private Watcher workWatcher = (event) -> {
        try {
            if (event.getType().equals(EventType.NodeDataChanged)) {
                String source = this.getContent(event.getPath(), WorkServer.this.workWatcher);
                this.handleSource(source);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    };
    
    public WorkServer(Configuration cfg) {
        this.cfg = cfg;
    }
    
    public void start(Subscribers subscribers) throws IOException, InterruptedException, KeeperException {
        if (null == subscribers) {
            throw new IllegalArgumentException("No subscribers registered...");
        }
        
        log.info("Id: " + cfg.getClientId() + " starting...");
        this.subscribers = subscribers;
        
        CountDownLatch latch = new CountDownLatch(1);
        zk = new ZooKeeper(cfg.getZkAddrs(), cfg.getSessionTimeout(), new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getState().equals(KeeperState.Expired)) {
                    try {
                        WorkServer.this.close();
                        WorkServer.this.start(WorkServer.this.subscribers);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    
                }
                
                if (event.getState().equals(KeeperState.SyncConnected)) {
                    latch.countDown();
                }
            }
            
        });
        
        latch.await();
        
        this.createWorkNode(cfg.getNamespace() + cfg.getWorkerNode() + Configuration.SEPARATOR + cfg.getClientId());
        log.info("Id: " + cfg.getClientId() + " work node created...");
        
        String source = this.getContent(cfg.getNamespace() + cfg.getWorkerNode() + Configuration.SEPARATOR + cfg.getClientId(), workWatcher);
        this.handleSource(source);
    }
    
    private void handleSource(String source) throws KeeperException, InterruptedException {
        List<String> groups = Worker.parseSource(source);
        this.assigned.clear();
        this.assigned.addAll(groups);
        
        if (!groups.isEmpty()) {
            for (String group : groups) {
                List<String> events = getChild(cfg.getNamespace() + cfg.getRequestNode() + Configuration.SEPARATOR + group, groupWatcher);
                log.info("id: " + cfg.getClientId() + " ready for listening to group: " + group);
                
                if (null != events && !events.isEmpty()) {
                    this.doJob(cfg.getNamespace() + cfg.getRequestNode() + Configuration.SEPARATOR + group, events);
                }
            }
        }
    }
    
    private boolean delZnode(String znode) 
            throws InterruptedException, KeeperException {
        try{
            zk.delete(znode, -1);
            return true;
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return delZnode(znode);
            } else if (e.code().equals(KeeperException.Code.NONODE)) {
                return false;
            } else {
                throw e;
            }
        }
    }
    
    private String getContent(String znode, Watcher watcher) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        try{
            byte[] source = zk.getData(znode, watcher, stat);
            return null == source ? null : new String(source);
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.getContent(znode, watcher);
            } else {
                throw e;
            }
        }
    }
    
    private List<String> getChild(String znode, Watcher watcher) throws KeeperException, InterruptedException {
        try{
            if (null == watcher) {
                return zk.getChildren(znode, false);
            } else {
                return zk.getChildren(znode, watcher);
            }
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.getChild(znode, watcher);
            } else {
                throw e;
            }
        }
    }
    
    private void createWorkNode(String znode) throws InterruptedException, KeeperException {
        try{
            zk.create(znode, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch(KeeperException e) {
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.createWorkNode(znode);
            } else {
                throw e;
            }
        }
    }
    
    private void close() {
        if (null != zk) {
            try {
                zk.close();
                zk = null;
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
