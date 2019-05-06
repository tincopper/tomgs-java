package com.tomgs.netty.proxy.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.StandardCharsets;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 14:41
 **/
public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel inbound;

    public ProxyBackendHandler(Channel inbound) {
        this.inbound = inbound;
    }

    //当代理服务与目标服务器的通道连接建立时
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与目标服务连接建立...");
        ctx.read();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>>>>>>>>>IN-ACTIVE>>>>>>>>>>");
        ProxyFrontendHandler.closeOnFlush(inbound);
    }

    /**
     * 处理目标服务的返回信息<br/>
     *
     * msg是从目标服务器返回的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Target server returns data:" + msg);
        /*ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        //将信息读到字节数组
        byteBuf.readBytes(bytes);
        String body = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("Target server returns data:" + body);*/
        /*
         * 接收目标服务器发送来的数据并打印
         * 然后把数据写入代理服务器和客户端的通道里
         */
        //通过inboundChannel向客户端写入数据
        inbound.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    inbound.close();
                } else {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ProxyFrontendHandler.closeOnFlush(ctx.channel());
    }
}
