package com.tomgs.learning.grpc.stream;

import com.tomgs.learning.grpc.proto.GreeterStreamGrpc;
import com.tomgs.learning.grpc.proto.HelloStreamReply;
import com.tomgs.learning.grpc.proto.HelloStreamRequest;
import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * HelloStreamClient
 *
 * @author tomgs
 * @since 2022/4/25
 */
@Slf4j
public class HelloStreamClient {

    public static void main(String[] args) {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051)
                //.executor()
                .usePlaintext()
                .build();
        final HelloStreamRequest request = HelloStreamRequest.newBuilder().setName("test").build();
        final ClientCall<HelloStreamRequest, HelloStreamReply> clientCall = channel.newCall(GreeterStreamGrpc.getSayHelloStreamMethod(), CallOptions.DEFAULT);
        ClientCalls.asyncServerStreamingCall(clientCall, request, new StreamObserver<HelloStreamReply>() {
            @Override
            public void onNext(HelloStreamReply reply) {
                log.info("stream reply: {}", reply);
            }

            @Override
            public void onError(Throwable t) {
                log.error("error: ", t);
            }

            @Override
            public void onCompleted() {
                log.info("stream completed");
            }
        });

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();

        channel.shutdownNow();
    }

}
