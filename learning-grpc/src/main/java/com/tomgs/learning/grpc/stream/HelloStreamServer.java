package com.tomgs.learning.grpc.stream;

import com.tomgs.learning.grpc.GrpcServiceRegistry;
import com.tomgs.learning.grpc.core.Event;
import com.tomgs.learning.grpc.core.EventProcessor;
import com.tomgs.learning.grpc.core.EventType;
import com.tomgs.learning.grpc.core.GrpcStartService;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * HelloStreamServer
 *
 * @author tomgs
 * @since 2022/4/25
 */
public class HelloStreamServer {

    public static void main(String[] args) {
        final GreeterStreamImpl greeterStream = new GreeterStreamImpl();
        GrpcServiceRegistry registry = new GrpcServiceRegistry();
        registry.addService(greeterStream);
        GrpcStartService grpcStartService = new GrpcStartService();
        grpcStartService.start();

        Thread eventProcessorThread = new Thread(() -> {
            EventProcessor eventProcessor = new EventProcessor(greeterStream);
            while (true) {
                Event event = new Event(EventType.Service);
                eventProcessor.notify(event);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "eventProcessorThread");
        eventProcessorThread.start();

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();

        grpcStartService.stop();
    }

}
