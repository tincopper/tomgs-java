/*
 * File Name:Worker.java
 * Author:ouyangliang2
 * Date:2017年8月10日
 * Copyright (C) 2006-2017
 */

package com.tomgs.zk.eventdriven;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author:ouyangliang2
 */
public class Worker {
    private String name;
    private List<String> groups;

    public Worker(String name, List<String> groups) {
        super();
        this.name = name;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public byte[] content() {
        
        if (null == groups || groups.isEmpty()) {
            return "".getBytes();
        }
        
        int size = groups.size();
        StringBuffer sb  = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(groups.get(i));
            if (i != size -1) {
                sb.append(Configuration.GROUP_SEPARATOR);
            }
        }
        
        return sb.toString().getBytes();
    }
    
    public static List<String> parseSource(String source) {
        if (null == source || source.isEmpty()) {
            return new LinkedList<>();
        }
        
        return new LinkedList<>(Arrays.asList(source.split(Configuration.GROUP_SEPARATOR)));
    }
}