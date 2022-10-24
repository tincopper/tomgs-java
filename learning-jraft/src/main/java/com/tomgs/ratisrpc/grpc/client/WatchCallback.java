package com.tomgs.ratisrpc.grpc.client;

import com.tomgs.learning.grpc.proto.DataChangeEvent;

/**
 * WatchCallback
 *
 * @author tomgs
 * @since 1.0
 */
public interface WatchCallback {

    void callback(DataChangeEvent event);

}
