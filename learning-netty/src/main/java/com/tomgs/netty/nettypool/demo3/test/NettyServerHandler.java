package com.tomgs.netty.nettypool.demo3.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static com.tomgs.netty.nettypool.demo2.NettyPoolClient.CLIENT_CHANNEL_KEY;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:30
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActived");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println(count.getAndIncrement() + ":" + body);
        ctx.writeAndFlush("Welcome to Netty.$_");
    }
}
