/*
 * File Name:Client.java
 * Author:ouyangliang2
 * Date:2017年8月11日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
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
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    
    private Configuration cfg;
    private ZooKeeper zk;
    
    public Client(Configuration cfg) throws InterruptedException, IOException {
        this.cfg = cfg;
        this.initZk();
    }
    
    public void publish(Event event) throws InterruptedException, KeeperException {
        String groupPath = cfg.getNamespace() + cfg.getRequestNode() + Configuration.SEPARATOR + event.getGroup();
        
        if (!this.znodeExists(groupPath)) {
            this.createGroupNode(groupPath);
        }
        
        this.createEventNode(event);
    }
    
    public void monitor() throws KeeperException, InterruptedException {
        List<String> workerList = this.getChildren(cfg.getWorkerNode());
        List<Worker> workers = new LinkedList<>();
        
        for (String w : workerList) {
            String source = this.getContent(cfg.getNamespace() + cfg.getWorkerNode() + Configuration.SEPARATOR + w);
            // 可能发生NONODE异常，这不是问题。
            // NONODE意味着某个Worker下线了，Master会收到通知，并重新进行分配。
            workers.add(new Worker(w, Worker.parseSource(source)));
        }
        
        List<String> groups  = this.getChildren(cfg.getRequestNode());
        
        StringBuilder sb = new StringBuilder(512);
        sb.append("当前所有的分组: ").append(groups.toString()).append("\r\n");
        sb.append("当前所有的Worker: ").append(workerList.toString()).append("\r\n");
        
        workers.forEach((w) -> {
            sb.append("Worker [").append(w.getName()).append("] 的分配情况: ").append(w.getGroups()).append("\r\n");
        });
        
        log.info(sb.toString());
    }
    
    private List<String> getChildren(String znode) throws KeeperException, InterruptedException {
        try{
            return zk.getChildren(cfg.getNamespace() + znode, false);
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.getChildren(znode);
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
    
    private void createGroupNode(String znode) throws InterruptedException, KeeperException {
        try{
            zk.create(znode, "".getBytes(), 
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.createGroupNode(znode);
            } if (e.code().equals(KeeperException.Code.NODEEXISTS)) {
                return;
            } else {
                throw e;
            }
        }
    }
    
    private void createEventNode(Event event) throws InterruptedException, KeeperException {
        try{
            zk.create(cfg.getNamespace() + cfg.getRequestNode() + Configuration.SEPARATOR + event.getGroup() + Configuration.SEPARATOR + "event", 
                    event.toJson().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        }
        catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.createEventNode(event);
            } if (e.code().equals(KeeperException.Code.NODEEXISTS)) {
                return;
            } else {
                throw e;
            }
        }
    }
    
    private boolean znodeExists(String znode) throws InterruptedException, KeeperException {
        try{
            Stat stat = zk.exists(znode, false);
            return null != stat;
        }
        catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.znodeExists(znode);
            } else {
                throw e;
            }
        }
    }
    
    private void initZk() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        zk = new ZooKeeper(cfg.getZkAddrs(), cfg.getSessionTimeout(), new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getState().equals(KeeperState.Expired)) {
                    Client.this.close();
                    
                    try{
                        Client.this.initZk();
                    } catch(Exception e){
                        log.error(e.getMessage(), e);
                    }
                } else if (event.getState().equals(KeeperState.SyncConnected)) {
                    latch.countDown();
                }
            }
            
        });
        
        latch.await();
    }
    
    public void close() {
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
