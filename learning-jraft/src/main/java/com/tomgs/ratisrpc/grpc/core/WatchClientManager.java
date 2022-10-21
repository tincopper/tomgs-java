package com.tomgs.ratisrpc.grpc.core;

import com.tomgs.ratis.kv.watch.DataChangeListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WatchClientManager
 *
 * @author tomgs
 * @since 1.0
 */
public class WatchClientManager {

    private static final Map<String, DataChangeListener> listenerMap = new ConcurrentHashMap<>();

    public void watch() {

    }

    public void unwatch() {

    }

}
