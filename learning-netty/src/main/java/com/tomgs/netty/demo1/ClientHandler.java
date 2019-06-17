package com.tomgs.netty.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 18:33
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("## Client to the proxy server, said: I was a client, give my regards to the target server!");
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = "I'm client, give my regards to the target server!".getBytes(Charset.forName("utf-8"));
        final int length = bytes.length;
        buffer.writeBytes(bytes);
        final int capacity = buffer.capacity();
        final int sss = buffer.writerIndex() - buffer.readerIndex();
        final int readableBytes = buffer.readableBytes();
        ChannelFuture f = ctx.writeAndFlush(buffer);
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
        final int length = mess.getBytes().length;
        System.out.println("## Receiving a message from the proxy server to:" + mess);
        ctx.close();
    }
}
