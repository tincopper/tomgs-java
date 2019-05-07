package com.tomgs.netty.proxy.server.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 14:26
 **/
public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    private String realHost = "10.18.4.23";
    private int realPort = 9300;

    //用于建立代理服务器与真实服务的通道，使用这个通道去与真实的服务器通信
    private Channel outbound;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("代理连接建立...");
        //建立与真实服务的连接
        Bootstrap bootstrap = new Bootstrap();
        Channel inbound = ctx.channel();
//        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //沿用之前通道的线程池
        bootstrap.group(inbound.eventLoop()).channel(inbound.getClass());
        bootstrap.option(ChannelOption.AUTO_READ, false);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        //new ObjectEncoder(),
                        //new ObjectDecoder(1315271800, ClassResolvers.cacheDisabled(null)),
                        new ProxyBackendHandler(inbound));
            }
        });

        /*
         * 连接目标服务器
         * ChannelFuture
         * Netty中的IO操作是异步的，
         * 包括bind、write、connect等操作会简单的返回一个ChannelFuture，调用者并不能立刻获得结果。
         * 当future对象刚刚创建时，处于非完成状态。可以通过isDone()方法来判断当前操作是否完成。
         * 通过isSuccess()判断已完成的当前操作是否成功，getCause()来获取已完成的当前操作失败的原因，isCancelled()来判断已完成的当前操作是否被取消。
         * 调用者可以通过返回的ChannelFuture来获取操作执行的状态，注册监听函数来执行完成后的操作。
         */
        ChannelFuture future = bootstrap.connect(realHost, realPort);

        /*
         * 获得代理服务器和目标服务器之间的连接通道
         */
        outbound = future.channel();
        //future.sync(); //加上这句会出现死锁 http://www.linkedkeeper.com/detail/blog.action?bid=1027

        /*
         * ChannelFutureListener
         * 监听ChannelFuture的状态
         * 注册监听函数来执行完成后的操作
         */
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // connection complete start to read first data
                    inbound.read();
                    System.out.println("outbound status::" + outbound.isActive());
                } else {
                    // Close the connection if the connection attempt has failed.
                    inbound.close();
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("代理连接断开...");
        if (outbound != null) {
            closeOnFlush(outbound);
        }
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("from client message:" + msg);

        /*ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        //将信息读到字节数组
        byteBuf.readBytes(bytes);
        String body = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("from client message:" + body);*/

        if (outbound.isActive()) {
            outbound.writeAndFlush(msg).addListener(new ChannelFutureListener() {
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
