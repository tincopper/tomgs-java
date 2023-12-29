/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomgs.learning.grpc.base;

import com.tomgs.learning.grpc.proto.GreeterGrpc;
import com.tomgs.learning.grpc.proto.common.HelloReply;
import com.tomgs.learning.grpc.proto.common.HelloRequest;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class HelloWorldServer {
  private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());

  private Server server;

  private void start() throws IOException {
    /* The port on which the server should run */
    int port = 8080;
    server = ServerBuilder.forPort(port)
        //.addService(ServerInterceptors.intercept(new GreeterImpl(), new MyServerInterceptor()))
        .addService(ServerInterceptors.intercept(new GreeterImpl(), new CustomServerInterceptor2()))
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        try {
          HelloWorldServer.this.stop();
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }

  static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    static final Metadata.Key<String> REQID_HEADER_KEY =
            Metadata.Key.of("request-id-1", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      /*RuntimeException e = new RuntimeException("test custom exception");
      //responseObserver.onError(e);

      responseObserver.onError(Status.INVALID_ARGUMENT
              // 这里就是我们的自定义异常信息
              .withDescription(e.getMessage())
              .withCause(e)
              .asRuntimeException());*/

      try {
        //HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        //responseObserver.onNext(reply);
        //responseObserver.onCompleted();
        throw new RuntimeException("test exception");
      } catch (Exception e) {
        Metadata metadata = new Metadata();
        metadata.put(REQID_HEADER_KEY, "123");
        responseObserver.onError(Status.INVALID_ARGUMENT
                // 这里就是我们的自定义异常信息
                .withDescription("{\"description\":\""+ e.getMessage() +"\",\"errcode\":1000001001}")
                .withCause(e.getCause())
                .asRuntimeException(metadata));
      }
    }

    @Override
    public void sayGood(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

  }
}
