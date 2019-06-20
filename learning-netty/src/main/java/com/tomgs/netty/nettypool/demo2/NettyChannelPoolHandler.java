package com.tomgs.netty.nettypool.demo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:25
 **/
public class NettyChannelPoolHandler implements ChannelPoolHandler {

    @Override
    public void channelReleased(Channel ch) throws Exception {
        //释放连接到池子里面
        System.out.println("channelReleased. Channel ID: " + ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        //从池子获取的连接
        System.out.println("channelAcquired. Channel ID: " + ch.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        //创建新的连接到池子
        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
        System.out.println("channelCreated. Channel ID: " + ch.id());
        SocketChannel channel = (SocketChannel) ch;
        channel.config().setKeepAlive(true);
        channel.config().setTcpNoDelay(true);
        channel.pipeline()
                .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                .addLast(new StringDecoder()).addLast(new StringEncoder()).addLast(new NettyClientHander());

    }
}
