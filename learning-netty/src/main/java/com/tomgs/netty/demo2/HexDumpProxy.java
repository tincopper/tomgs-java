package com.tomgs.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 代理服务器<br/>
 * 1. 没有对请求内容进行解析操作<br/>
 * 2. 可以代理http和tcp请求都没有问题
 *
 * @author tangzhongyuan
 * @since 2019-05-06 15:08
 **/
public final class HexDumpProxy {

    static final int LOCAL_PORT = Integer.parseInt(System.getProperty("localPort", "8080"));
    static final String REMOTE_HOST = System.getProperty("remoteHost", "10.18.4.23");
    static final int REMOTE_PORT = Integer.parseInt(System.getProperty("remotePort", "9300"));

    public static void main(String[] args) throws Exception {
        System.err.println("Proxying *:" + LOCAL_PORT + " to " + REMOTE_HOST + ':' + REMOTE_PORT + " ...");

        // Configure the bootstrap.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HexDumpProxyInitializer(REMOTE_HOST, REMOTE_PORT))
                    .childOption(ChannelOption.AUTO_READ, false)
                    .bind(LOCAL_PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}