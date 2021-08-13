/*
 * File Name:Subscribers.java
 * Author:ouyangliang2
 * Date:2017年8月8日
 * Copyright (C) 2006-2017
 */
 
package com.tomgs.zk.eventdriven;

import java.util.List;

/**
 * @author:ouyangliang2
 */
public interface Subscribers {
    public List<BaseSubscriber> getSubscribers(String eventType);
}
