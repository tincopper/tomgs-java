package com.tomgs.es.gateway.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public enum HttpUtils {
    INSTANCE;

    public boolean isBadResponse(HttpResponse httpResponse) {
        return httpResponse.status().code() > 300;
    }

    public FullHttpResponse createFullHttpResponse(HttpVersion httpVersion,
                                                   HttpResponseStatus status,
                                                   String body) {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ByteBuf content = Unpooled.copiedBuffer(bytes);

        return createFullHttpResponse(httpVersion, status, "text/html; charset=utf-8", content, bytes.length);
    }

    public FullHttpResponse createFullHttpResponse(HttpVersion httpVersion,
                                                   HttpResponseStatus status,
                                                   String contentType,
                                                   ByteBuf body,
                                                   int contentLength) {
        DefaultFullHttpResponse response;

        if (body != null) {
            response = new DefaultFullHttpResponse(httpVersion, status, body);
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, contentLength);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, contentType);
        } else {
            response = new DefaultFullHttpResponse(httpVersion, status);
        }

        return response;
    }
    public FullHttpResponse createTimeoutResponse() {
        return createFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.GATEWAY_TIMEOUT,"response timeout!!!");
    }
}
