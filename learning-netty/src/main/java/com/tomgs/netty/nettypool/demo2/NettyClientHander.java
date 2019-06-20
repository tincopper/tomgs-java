package com.tomgs.netty.nettypool.demo2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

import static com.tomgs.netty.nettypool.demo2.NettyPoolClient.CLIENT_CHANNEL_KEY;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:27
 **/
public class NettyClientHander extends ChannelInboundHandlerAdapter {

    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Integer index = ctx.channel().attr(CLIENT_CHANNEL_KEY).get();
        System.out.println(index + ":" + msg);
    }
}
