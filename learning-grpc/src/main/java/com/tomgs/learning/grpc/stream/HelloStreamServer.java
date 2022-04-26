package com.tomgs.learning.grpc.stream;

import com.tomgs.learning.grpc.GrpcServiceRegistry;
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
        GrpcServiceRegistry registry = new GrpcServiceRegistry();
        registry.addService(new GreeterStreamImpl());
        GrpcStartService grpcStartService = new GrpcStartService();
        grpcStartService.start();

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();

        grpcStartService.stop();
    }

}
