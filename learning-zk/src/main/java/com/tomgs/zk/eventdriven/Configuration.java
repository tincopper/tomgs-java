/*
 * File Name:Config.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */

package com.tomgs.zk.eventdriven;

/**
 * @author:ouyangliang2
 */
public class Configuration {
    public static final String SEPARATOR = "/";
    public static final String GROUP_SEPARATOR = ":";
    
    private String namespace;
    private String masterNode = "master";
    private String workerNode = "workers";
    private String requestNode = "request";
    
    private String zkAddrs;
    private int sessionTimeout;
    private String clientId;

    public String getZkAddrs() {
        return zkAddrs;
    }

    public void setZkAddrs(String zkAddrs) {
        this.zkAddrs = zkAddrs;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace.endsWith(SEPARATOR) ? namespace : namespace + SEPARATOR;
    }

    public String getMasterNode() {
        return masterNode;
    }

    public void setMasterNode(String masterNode) {
        this.masterNode = masterNode;
    }

    public String getWorkerNode() {
        return workerNode;
    }

    public void setWorkerNode(String workerNode) {
        this.workerNode = workerNode;
    }

    public String getRequestNode() {
        return requestNode;
    }

    public void setRequestNode(String requestNode) {
        this.requestNode = requestNode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

}
