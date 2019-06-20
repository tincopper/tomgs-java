package com.tomgs.es.gateway.transport.http.netty4.v2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.net.SocketAddress;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:40
 **/
public enum ConnectionPoolManager implements Closeable {

    INSTANCE;

    private final Bootstrap bootstrap = new Bootstrap();
    private final AbstractChannelPoolMap<SocketAddress, ChannelPool> poolMap;

    ConnectionPoolManager() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        poolMap = new AbstractChannelPoolMap<SocketAddress, ChannelPool>() {
            @Override
            protected ChannelPool newPool(SocketAddress key) {
                return new FixedChannelPool(bootstrap.remoteAddress(key),
                        new HttpChannelPoolHandler(ConnectionPoolManager.INSTANCE), 100);
            }
        };
    }

    public ChannelPool getChannel(SocketAddress address) {
        return poolMap.get(address);
    }

    public void release(Channel channel) {
        final SocketAddress socketAddress = channel.remoteAddress();
        if (poolMap.contains(socketAddress)) {
            poolMap.get(socketAddress).release(channel);
        }
    }

    @Override
    public void close() {
        poolMap.close();
    }
}
