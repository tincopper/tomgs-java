package com.tomgs.es.gateway.transport.http.netty4.v2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.ReferenceCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpHandlerContent {

    private static final Logger log = LoggerFactory.getLogger(HttpHandlerContent.class);

    private HttpRequest request;
    private HttpResponse response;
    private Channel clientChannel;
    private int requestDataSize;

    private Map<String, Object> attributes = new HashMap<>();
    private volatile AtomicBoolean isResponse = new AtomicBoolean(false);

    public HttpHandlerContent(HttpRequest httpRequest, HttpResponse httpResponse, Channel clientChannel, int requestDataSize) {
        this.request = httpRequest;
        this.response = httpResponse;
        this.clientChannel = clientChannel;
        this.requestDataSize = requestDataSize;
    }

    public HttpHandlerContent(HttpRequest httpRequest, Channel clientChannel, int requestDataSize) {
        this.request = httpRequest;
        this.clientChannel = clientChannel;
        this.requestDataSize = requestDataSize;
    }

    public <T> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    public <T> T getAttribute(String name, T def) {
        if (Objects.isNull(getAttribute(name))) {
            return def;
        }
        return (T) attributes.get(name);
    }

    public <T> void attribute(String name, T t) {
        attributes.put(name, t);
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public AtomicBoolean isResponse() {
        return isResponse;
    }

    public void response(HttpResponse httpResponse) {
        try {
            if (!Objects.isNull(httpResponse)) {
                this.response = httpResponse;
            }
        } catch (Exception e) {
            log.error("response throws exceptions, msg:[{}].", e.getMessage(), e);
        } finally {
            writeToChannel(clientChannel, response);
            isResponse.compareAndSet(false, true);
        }

    }

    public void response() {
        response(null);
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public int getRequestDataSize() {
        return requestDataSize;
    }

    public void setRequestDataSize(int requestDataSize) {
        this.requestDataSize = requestDataSize;
    }

    /**
     * 如果msg是ReferenceCounted的实例话 这里的引用计数器应该加1，否则其他线程计算出引用为0而回收
     * 该对象，导致异常io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
     */
    private ChannelFuture writeToChannel(Channel clientChannel, final Object msg) {
        if (msg instanceof ReferenceCounted) {
            log.debug("Retaining reference counted message");
            ((ReferenceCounted) msg).retain();
        }
        return clientChannel.writeAndFlush(msg);
    }

}
