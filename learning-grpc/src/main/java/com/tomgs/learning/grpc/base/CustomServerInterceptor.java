package com.tomgs.learning.grpc.base;

import io.grpc.*;

/**
 * CustomServerInterceptor
 *
 * @author tomgs
 * @since 1.0
 */

public class CustomServerInterceptor implements ServerInterceptor {

    static final Metadata.Key<String> REQID_HEADER_KEY =
            Metadata.Key.of("request-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        ServerCall.Listener<ReqT> listener;

        try {
            // 调用下一个拦截器或服务实现
            listener = next.startCall(call, headers);
        } catch (Exception ex) {
            // 拦截异常并进行自定义处理
            Status status = Status.INTERNAL.withDescription("Custom error message");
            headers.put(REQID_HEADER_KEY, "test");
            call.close(status, headers);
            return new ServerCall.Listener<ReqT>() {
            };
        }

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            @Override
            public void onMessage(ReqT message) {
                // log req message
                System.out.println("Custom log request message: " + message);
                super.onMessage(message);
            }

            @Override
            public void onHalfClose() {
                try {
                    //this.delegate().onHalfClose();
                    super.onHalfClose();
                } catch (Exception ex) {
                    // 在异常发生时进行自定义处理
                    Status status = Status.INTERNAL.withDescription("Custom error message");
                    headers.put(REQID_HEADER_KEY, "test1");
                    call.close(status, headers);
                }
            }
        };
    }
}
