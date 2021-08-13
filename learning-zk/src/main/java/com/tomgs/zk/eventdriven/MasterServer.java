/*
 * File Name:EventServer.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author:ouyangliang2
 */
public class MasterServer {
    static final Logger log = LoggerFactory.getLogger(MasterServer.class);
    
    private Configuration cfg;
    private ZooKeeper zk;
    private SimpleLock lock;
    private Assigner assigner;
    
    private Watcher masterWatcher = (event) -> {
        if (event.getType().equals(EventType.NodeChildrenChanged)) {
            try {
                if (event.getPath().endsWith(cfg.getRequestNode())) {
                    MasterServer.this.onNewGroup();
                } else if (event.getPath().endsWith(cfg.getWorkerNode())) {
                    MasterServer.this.onWorkerChange();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    };
    
    public MasterServer(Configuration cfg, Assigner assigner) {
        this.cfg = cfg;
        this.assigner = assigner;
    }
    
    public void start() throws IOException, InterruptedException, KeeperException {
        CountDownLatch latch = new CountDownLatch(1);
        zk = new ZooKeeper(cfg.getZkAddrs(), cfg.getSessionTimeout(), new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getState().equals(KeeperState.Expired)) {
                    try {
                        MasterServer.this.close();
                        MasterServer.this.start();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    
                } else if (event.getState().equals(KeeperState.SyncConnected)) {
                    latch.countDown();
                }
            }
            
        });
        
        latch.await();
        lock = new SimpleLock(zk, cfg.getNamespace());
        lock.lock(cfg.getClientId(), cfg.getMasterNode());
        log.info("Now it is the master server...");
        
        getChildren(cfg.getWorkerNode(), masterWatcher);
        log.info("ready for listening to workers...");
        
        getChildren(cfg.getRequestNode(), masterWatcher);
        log.info("ready for listening to requests...");
    }
    
    private void onWorkerChange() throws KeeperException, InterruptedException {
        List<String> workerList = this.getChildren(cfg.getWorkerNode(), masterWatcher);
        List<Worker> workers = new LinkedList<>();
        
        for (String w : workerList) {
            // 这里需要重新为每个worker分配，所以不需要知道原有的分配情况。
            workers.add(new Worker(w, new LinkedList<>()));
        }
        
        List<String> groups  = this.getChildren(cfg.getRequestNode(), masterWatcher);
        Situation s = new Situation(cfg, groups, workers);
        
        synchronized(MasterServer.class) {
            //新的group产生，或者worker上下线都会触发onChange方法进行重新分配。
            //为了避免并发引起的各种问题，这里加上同步。
            assigner.onWorkerChange(s, zk);
        }
    }
    
    private void onNewGroup() throws KeeperException, InterruptedException {
        List<String> workerList = this.getChildren(cfg.getWorkerNode(), masterWatcher);
        List<Worker> workers = new LinkedList<>();
        
        for (String w : workerList) {
            String source = null;
            try {
                source = this.getContent(cfg.getNamespace() + cfg.getWorkerNode() + Configuration.SEPARATOR + w);
            } catch (KeeperException.NoNodeException e) {
                // 可能发生NONODE异常，这不是问题。
                // NONODE意味着某个Worker下线了，Master会收到通知，并重新进行分配。
                // 所以本次分配可以退出，onWorkerChange()方法会被触发
                return;
            }
            
            workers.add(new Worker(w, Worker.parseSource(source)));
        }
        
        List<String> groups  = this.getChildren(cfg.getRequestNode(), masterWatcher);
        Situation s = new Situation(cfg, groups, workers);
        
        synchronized(MasterServer.class) {
            //新的group产生，或者worker上下线都会触发onChange方法进行重新分配。
            //为了避免并发引起的各种问题，这里加上同步。
            assigner.onNewGroup(s, zk);
        }
    }
    
    private List<String> getChildren(String znode, Watcher watcher) throws KeeperException, InterruptedException {
        try{
            if (null == watcher) {
                return zk.getChildren(cfg.getNamespace() + znode, false);
            } else {
                return zk.getChildren(cfg.getNamespace() + znode, watcher);
            }
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                if (null == watcher) {
                    return zk.getChildren(cfg.getNamespace() + znode, false);
                } else {
                    return zk.getChildren(cfg.getNamespace() + znode, watcher);
                }
            } else {
                throw e;
            }
        }
    }
    
    private String getContent(String znode) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        try{
            byte[] source = zk.getData(znode, false, stat);
            return null == source ? null : new String(source);
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.getContent(znode);
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
