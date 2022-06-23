package com.tomgs.learning.grpc.base;

import io.grpc.*;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * MyServerInsterceptor
 *
 * @author tomgs
 * @since 2022/4/29
 */
@Slf4j
public class MyServerInterceptor implements ServerInterceptor {

    static final Metadata.Key<String> CUSTOM_HEADER_KEY =
            Metadata.Key.of("Custom-Server-Header-Key", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata requestHeaders, ServerCallHandler<ReqT, RespT> next) {
        log.info("ServiceName:{}", call.getMethodDescriptor().getServiceName());
        requestHeaders.keys().forEach(t -> {
            String val = requestHeaders.get(Metadata.Key.of(t, Metadata.ASCII_STRING_MARSHALLER));
            log.info("key:{},val:{}", t, val);

        });

        //获取客户端参数
        Metadata.Key<String> token = Metadata.Key.of("test", Metadata.ASCII_STRING_MARSHALLER);

        String tokenStr = requestHeaders.get(token);
        if (StringUtil.isNullOrEmpty(tokenStr)) {
            System.err.println("没有接收到相应的Token值");
        }
        //return next.startCall(call, headers);
        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                // @2 响应客户端设置服务端Header信息
                responseHeaders.put(CUSTOM_HEADER_KEY, "customRespondValue");
                super.sendHeaders(responseHeaders);
            }
        }, requestHeaders);
    }

}
