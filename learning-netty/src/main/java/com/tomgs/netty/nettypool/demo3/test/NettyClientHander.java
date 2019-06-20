package com.tomgs.netty.nettypool.demo3.test;

import com.tomgs.netty.nettypool.demo3.ChannelPoolMap;
import com.tomgs.netty.nettypool.demo3.CustomerChannel;
import com.tomgs.netty.nettypool.demo3.CustomerFixedChannelPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tomgs.netty.nettypool.demo2.NettyPoolClient.CLIENT_CHANNEL_KEY;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:27
 **/
public class NettyClientHander extends ChannelInboundHandlerAdapter {

    static AtomicInteger count = new AtomicInteger(1);
    private final ChannelPoolMap<SocketAddress, CustomerFixedChannelPool> poolMap;

    public NettyClientHander(ChannelPoolMap<SocketAddress, CustomerFixedChannelPool> poolMap) {
        this.poolMap = poolMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Integer index = ctx.channel().attr(CLIENT_CHANNEL_KEY).get();
        System.out.println(index + ":" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //释放连接
        System.out.println(ctx);
        final SocketAddress localAddress = ctx.channel().localAddress();
        final SocketAddress remoteAddress = ctx.channel().remoteAddress();
        final CustomerChannel customerChannel = CustomerChannel.builder().channel(ctx.channel()).build();
        poolMap.get(remoteAddress).release(customerChannel);
    }
}
