package com.tomgs.learning.grpc.base;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
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

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        log.info("ServiceName:{}", call.getMethodDescriptor().getServiceName());
        headers.keys().forEach(t -> {
            String val = headers.get(Metadata.Key.of(t, Metadata.ASCII_STRING_MARSHALLER));
            log.info("key:{},val:{}", t, val);

        });

        //获取客户端参数
        Metadata.Key<String> token = Metadata.Key.of("test", Metadata.ASCII_STRING_MARSHALLER);

        String tokenStr = headers.get(token);
        if (StringUtil.isNullOrEmpty(tokenStr)) {
            System.err.println("没有接收到相应的Token值");
        }
        return next.startCall(call, headers);
    }

}
