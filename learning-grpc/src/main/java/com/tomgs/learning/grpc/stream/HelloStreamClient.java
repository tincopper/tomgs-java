package com.tomgs.learning.grpc.stream;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * HelloStreamClient
 *
 * @author tomgs
 * @since 2022/4/25
 */
public class HelloStreamClient {

    public static void main(String[] args) {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051)
                //.executor()
                .usePlaintext()
                .build();
    }

}
