package com.tomgs.netty.test.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 18:33
 **/
public class ClientHandler2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("## Client to the proxy server, said: I was a client, give my regards to the target server!");
        ChannelFuture f = ctx.writeAndFlush("I'm client, give my regards to the target server!");
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("===========write message success==========");
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String mess = (String) msg;
        System.out.println("## Receiving a message from the proxy server to:" + mess);
        ctx.close();
    }
}
