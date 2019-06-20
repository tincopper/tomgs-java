package com.tomgs.es.gateway.transport.http.netty4.v2;

import io.netty.channel.Channel;

public interface HttpHandler {

    void preDispatcherHandler(Channel channel);

    void dispatcherHandler(HttpHandlerContent requestContent);

    void postDispatcherHandler(Channel channel);
}
