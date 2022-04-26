package com.tomgs.learning.grpc.core;

/**
 * Service
 *
 * @author tomgs
 * @since 2022/4/25
 */
public interface Service {

    String getName();

    void start();

    void stop();

    boolean isStarted();

}
