package com.tomgs.netty.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 18:35
 **/
public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    private final String remoteHost;
    private final int remotePort;

    //代理服务器和目标服务器之间的通道（从代理服务器出去所以是outbound过境）
    private volatile Channel outboundChannel;


    /**
     * remoteHost和remotePort表示目标服务器
     *
     * @param remoteHost
     * @param remotePort
     */
    public ProxyFrontendHandler(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    /**
     * 当客户端和代理服务器建立通道连接时，调用此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 客户端和代理服务器的连接通道
         * 入境的通道
         */
        final Channel inboundChannel = ctx.channel();

        // Start the connection attempt.
        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
                .channel(ctx.channel().getClass())
                .option(ChannelOption.AUTO_READ, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new ProxyBackendHandler(inboundChannel)
                        );
                    }
                });

        /**
         * 连接目标服务器
         * ChannelFuture
         * Netty中的IO操作是异步的，
         * 包括bind、write、connect等操作会简单的返回一个ChannelFuture，调用者并不能立刻获得结果。
         * 当future对象刚刚创建时，处于非完成状态。可以通过isDone()方法来判断当前操作是否完成。通过isSuccess()判断已完成的当前操作是否成功，getCause()来获取已完成的当前操作失败的原因，isCancelled()来判断已完成的当前操作是否被取消。
         * 调用者可以通过返回的ChannelFuture来获取操作执行的状态，注册监听函数来执行完成后的操作。
         */
        ChannelFuture f = b.connect(remoteHost, remotePort);

        /*
         * 获得代理服务器和目标服务器之间的连接通道
         */
        outboundChannel = f.channel();

        /*
         * ChannelFutureListener
         * 监听ChannelFuture的状态
         * 注册监听函数来执行完成后的操作
         */
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // connection complete start to read first data
                    inboundChannel.read();
                } else {
                    // Close the connection if the connection attempt has failed.
                    inboundChannel.close();
                }
            }
        });
    }


    /**
     * 在这里接收客户端的消息
     * 在客户端和代理服务器建立连接时，也获得了代理服务器和目标服务器的通道outbound，
     * 通过outbound写入消息到目标服务器
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("## from client message:" + msg.toString());

        if (outboundChannel.isActive()) {
            String proxyToServer = "I am a proxy server, the client asked me to say hello to you.";
            outboundChannel.writeAndFlush(proxyToServer).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // was able to flush out data, start to read the next chunk
                        ctx.channel().read();
                    } else {
                        future.channel().close();
                    }
                }
            });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
