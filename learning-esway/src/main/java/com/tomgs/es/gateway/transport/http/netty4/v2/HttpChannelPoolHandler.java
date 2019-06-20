package com.tomgs.es.gateway.transport.http.netty4.v2;

import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:35
 **/
public class HttpChannelPoolHandler implements ChannelPoolHandler {

    private static final Logger log = LoggerFactory.getLogger(HttpChannelPoolHandler.class);

    private final ConnectionPoolManager manager;

    public HttpChannelPoolHandler(ConnectionPoolManager manager) {
        this.manager = manager;
    }

    @Override
    public void channelReleased(Channel ch) throws Exception {
        log.debug("channelReleased. Channel ID: [{}]", ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        log.debug("channelAcquired. Channel ID: [{}]", ch.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        log.debug("channelCreated. Channel ID: [{}]", ch.id());
        ch.pipeline().addLast(
                //new LoggingHandler(LogLevel.INFO),
                new HttpClientCodec(),
                new HttpObjectAggregator(65536),
                new HttpProxyBackendHandler(manager));
    }
}
