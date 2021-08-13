/*
 * File Name:Locker.java
 * Author:ouyangliang2
 * Date:2017年7月24日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Semaphore;

/**
 * @author:ouyangliang2
 */
public class SimpleLock {
    private static final String SEPARATOR = "/";
    private ZooKeeper zk;
    private String root;
    
    public SimpleLock(ZooKeeper zk, String root) {
        this.zk = zk;
        this.root = root.endsWith(SEPARATOR) ? root : root + SEPARATOR;
    }
    
    public boolean tryLock(String clientId, String resource) 
            throws KeeperException, InterruptedException {
        try{
            zk.create(root + resource, clientId.getBytes(), 
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            
            return true;
        } catch(KeeperException e) {
            if (e.code().equals(KeeperException.Code.NODEEXISTS)) {
                return false;
            } else if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.tryLockWhenConnectionLoss(clientId, resource);
            } else {
                throw e;
            }
        }
    }
    
    private boolean tryLockWhenConnectionLoss(String clientId, String resource) 
            throws KeeperException, InterruptedException {
        
        try{
            zk.create(root + resource, clientId.getBytes(), 
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            
            return true;
        } catch(KeeperException e) {
            if (e.code().equals(KeeperException.Code.NODEEXISTS)) {
                return this.checkNode(clientId, resource);
            } else if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.tryLockWhenConnectionLoss(clientId, resource);
            } else {
                throw e;
            }
        }
    }
    
    private boolean checkNode(String clientId, String resource) throws KeeperException, InterruptedException {
        try {
            Stat stat = new Stat();
            byte[] data = zk.getData(root + resource, false, stat);
            if (clientId.equals(new String(data))) {
                return true;
            }
            
            return false;
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.NONODE)) {
                return this.tryLock(clientId, resource);
            } else if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                return this.checkNode(clientId, resource);
            } else {
                throw e;
            }
        }
    }
    
    public void release(String clientId, String resource) throws KeeperException, InterruptedException {
        try{
            zk.delete(root + resource, -1);
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.checkRelease(clientId, resource);
            } else {
                throw e;
            }
        }
    }
    
    private void checkRelease(String clientId, String resource) throws KeeperException, InterruptedException {
        try {
            Stat stat = new Stat();
            byte[] data = zk.getData(root + resource, false, stat);
            if (clientId.equals(new String(data))) {
                this.release(clientId, resource);
            }
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.NONODE)) {
                return;
            } else if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.checkRelease(clientId, resource);
            } else {
                throw e;
            }
        }
    }
    
    public void lock(String clientId, String resource) throws KeeperException, InterruptedException {
        while (true) {
            if (this.tryLock(clientId, resource)) {
                return;
            }
            
            this.listenLock(resource);
        }
    }
    
    private void listenLock(String resource) throws InterruptedException, KeeperException {
        Semaphore s = new Semaphore(0);
        
        try {
            Stat stat = zk.exists(root + resource, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType().equals(EventType.NodeDeleted)) {
                        s.release();
                    }
                }
            });
            
            if (null != stat) {
                s.acquire();
            }
            
        } catch (KeeperException e) {
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.listenLock(resource);
                return;
            } else {
                throw e;
            }
        }
    }
    
}
