package com.tomgs.es.gateway.transport.http.netty4.v2;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 自定义http返回状态信息，用于异常信息返回给客户端
 *
 * @author tangzhongyuan
 * @since 2019-06-14 10:21
 **/
public class GateWayResponseStatus extends HttpResponseStatus {

    public static final HttpResponseStatus ES_CONNECT_REFUSED = newStatus(600, "http proxy connect es node refused");

    public static final HttpResponseStatus ES_NODE_NOT_FOUND = newStatus(601, "http proxy not found es node");

    public static final HttpResponseStatus ES_NODE_REQUEST_ERROR = newStatus(602, "http proxy send request to es node has exception");

    public static final HttpResponseStatus PROXY_RESPONSE_ERROR = newStatus(603, "http proxy response to client error");

    public GateWayResponseStatus(int code, String reasonPhrase) {
        super(code, reasonPhrase);
    }

    private static HttpResponseStatus newStatus(int statusCode, String reasonPhrase) {
        return HttpResponseStatus.valueOf(statusCode, reasonPhrase);
    }

}
