package com.tomgs.learning.grpc.base;

import com.google.protobuf.Duration;
import com.google.protobuf.util.Durations;
import io.grpc.*;
import io.grpc.binarylog.v1.GrpcLogEntry;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CustomServerInterceptor
 *
 * @see io.grpc.protobuf.services.BinlogHelper
 *
 * @author tomgs
 * @since 1.0
 */

public class CustomServerInterceptor2 implements ServerInterceptor {

    static final Metadata.Key<String> REQID_HEADER_KEY =
            Metadata.Key.of("request-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        System.out.println("Log ServerInfo, " + headers);
        ServerCall<ReqT, RespT> wCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendMessage(RespT message) {
                System.out.println("Log sendMessage header, " + headers);
                System.out.println("Log sendMessage response, " + message);

                super.sendMessage(message);
            }

            @Override
            public void sendHeaders(Metadata headers) {
                System.out.println("Log sendHeaders header, " + headers);
                headers.put(REQID_HEADER_KEY, "test2");
                super.sendHeaders(headers);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                System.out.println("Log close status, " + status);
                System.out.println("Log close trailers, " + trailers);

                super.close(status, trailers);
            }
        };

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(next.startCall(wCall, headers)) {
            @Override
            public void onMessage(ReqT message) {
                System.out.println("Log onMessage headers, " + headers);
                System.out.println("Log onMessage message, " + message);

                super.onMessage(message);
            }

            @Override
            public void onHalfClose() {
                System.out.println("Log onHalfClose headers, " + headers);

                super.onHalfClose();
            }

            @Override
            public void onCancel() {
                System.out.println("Log onCancel headers, " + headers);

                super.onCancel();
            }
        };
    }
}
