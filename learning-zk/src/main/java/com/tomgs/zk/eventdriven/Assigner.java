/*
 * File Name:Assigner.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.ZooKeeper;

/**
 * @author:ouyangliang2
 */
public interface Assigner {
    void onNewGroup(Situation s, ZooKeeper zk);
    
    void onWorkerChange(Situation s, ZooKeeper zk);
}
