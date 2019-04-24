package com.tomgs.guice.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String key = "requestId";
        Channel channel = ctx.channel();
        String requestId = channel.attr(AttributeKey.<String>valueOf(key)).get();
        System.out.println("======" + requestId);
        //1.
        //处理通道中接受到的信息
        //...
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        //将信息读到字节数组
        buf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");

        System.out.println("接收到客户端：" + body);

        String result = "[Server response:]" + body;
        //创建一个新的ByteBuf
        ByteBuf response = Unpooled.copiedBuffer(result.getBytes());
        /*
         *   处理完之后异步发送应答消息给客户端。
         *   为防止频繁的唤醒Selector进行消息的发送，Netty的write方法并不直接将消息写入到SocketChannel中，而是把待发送的消息
         *   发送到缓冲数组中去，再通过调用flush方法，将消息发送到SocketChannel中去。
         */
        ctx.write(response);//这个写是写的ByteBuf
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //2.
        //刷新缓冲区，将消息发送队列中的消息写入到SocketChannel中发送给客户端
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //3.
        //出现异常，关闭资源
        cause.printStackTrace();
        //会释放和ChannelHandlerContext关联的资源
        ctx.close();
    }

}
