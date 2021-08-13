/*
 * File Name:Event.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */

package com.tomgs.zk.eventdriven;

import com.google.gson.Gson;

/**
 * @author:ouyangliang2
 */
public class Event {
    private long id;
    private String type;
    private String val;
    private String group;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String toJson() {
        Gson g = new Gson();
        return g.toJson(this);
    }
    
    public static Event fromJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, Event.class);
    }

}
