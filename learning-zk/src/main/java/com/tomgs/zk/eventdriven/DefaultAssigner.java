/*
 * File Name:DefaultAssigner.java
 * Author:ouyangliang2
 * Date:2017年8月9日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.*;

/**
 * @author:ouyangliang2
 */
public class DefaultAssigner implements Assigner {

    @Override
    public void onNewGroup(Situation s, ZooKeeper zk) {
        List<String> unassignedGroups = new LinkedList<>();
        Map<String, String> reversedAssigned = s.reversedAssigned();
        
        s.getGroups().forEach((g)->{
            if (!reversedAssigned.containsKey(g)) {
                unassignedGroups.add(g);
            }
        });
        
        if (unassignedGroups.isEmpty()) {
            return;
        }
        
        MasterServer.log.info("Pending groups: " + unassignedGroups);
        
        List<Worker> workers = s.getWorkers();
        if (null == workers || workers.isEmpty()) {
            MasterServer.log.warn("No worker detected ...");
            return;
        }
        
        Set<Worker> affectedWorkers = new HashSet<>();
        
        unassignedGroups.forEach((g) -> {
            Collections.sort(workers, (w1, w2) -> w1.getGroups().size() - w2.getGroups().size());
            
            workers.get(0).getGroups().add(g);
            affectedWorkers.add(workers.get(0));
            
            MasterServer.log.info("Prepare to assign " + g + " to " + workers.get(0).getName() );
        });
        
        affectedWorkers.forEach((w) -> {
            try{
                this.refresh(w, zk, s.getCfg());
                // 可能发生NONODE异常，这不是问题。
                // NONODE意味着某个Worker下线了，Master会收到通知，并重新进行分配。
                MasterServer.log.info("Assigned groups: " + w.getGroups() + " to " + w.getName() + ".");
            } catch(Exception e){
                MasterServer.log.error(e.getMessage(), e);
            }
        });
        
        MasterServer.log.info("Finished...");
    }

    private void refresh(Worker worker, ZooKeeper zk, Configuration cfg) throws KeeperException, InterruptedException {
        try{
            zk.setData(cfg.getNamespace() + cfg.getWorkerNode() + Configuration.SEPARATOR + worker.getName(), worker.content(), -1);
        } catch(KeeperException e){
            if (e.code().equals(KeeperException.Code.CONNECTIONLOSS)) {
                this.refresh(worker, zk, cfg);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void onWorkerChange(Situation s, ZooKeeper zk) {
        List<String> groups = s.getGroups();
        if (null == groups || groups.isEmpty()) {
            MasterServer.log.info("No group found...");
            return;
        }
        
        MasterServer.log.info("All groups: " + groups);
        List<Worker> workers = s.getWorkers();
        
        if (null == workers || workers.isEmpty()) {
            MasterServer.log.warn("No worker detected...");
            return;
        }
        
        groups.forEach((g) -> {
            Collections.sort(workers, (w1, w2) -> w1.getGroups().size() - w2.getGroups().size());
            workers.get(0).getGroups().add(g);
        });
        
        workers.forEach((w) -> {
            try{
                this.refresh(w, zk, s.getCfg());
                MasterServer.log.info("Assigned groups: " + w.getGroups() + " to " + w.getName() + ".");
            } catch(Exception e){
                MasterServer.log.error(e.getMessage(), e);
                if (e instanceof KeeperException.NoNodeException) {
                    // 可能发生NONODE异常，这不是问题。
                    // NONODE意味着某个Worker下线了，Master会收到通知，并重新进行分配。
                    return;
                }
            }
        });
        
        MasterServer.log.info("Finished...");
    }
    
}
