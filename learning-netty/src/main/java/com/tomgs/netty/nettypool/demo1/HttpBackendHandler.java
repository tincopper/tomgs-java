package com.tomgs.netty.nettypool.demo1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:09
 **/
public class HttpBackendHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    public HttpBackendHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Backend Handler is Active!");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        System.out.println("read0: " + msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Backend Handler destroyed!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            ctx.channel().writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}