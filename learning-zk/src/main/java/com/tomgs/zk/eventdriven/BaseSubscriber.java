/*
 * File Name:BaseSubscriber.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

/**
 * @author:ouyangliang2
 */
public interface BaseSubscriber {
    void onEvent(Event event);
}
