/*
 * File Name:Situation.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */

package com.tomgs.zk.eventdriven;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:ouyangliang2
 */
public class Situation {
    private Configuration cfg;
    private List<String> groups;
    private List<Worker> workers;
    
    public Situation(Configuration cfg, List<String> groups, List<Worker> workers) {
        this.cfg = cfg;
        this.groups = groups;
        this.workers = workers;
    }
    
    public Map<String, String> reversedAssigned() {
        Map<String, String> rlt = new HashMap<>();
        if (null != workers) {
            for (Worker worker : workers) {
                for (String group : worker.getGroups()) {
                    rlt.put(group, worker.getName());
                }
            }
        }
        
        return rlt;
    }
    
    public Configuration getCfg() {
        return cfg;
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

}
