package com.tomgs.es.gateway.transport.tcp.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 14:21
 **/
public class NettyTcpServer {

    private final String host;
    private final int port;

    private final EventLoopGroup bossThreadPool;
    private final EventLoopGroup workerThreadPool;

    public NettyTcpServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.bossThreadPool = new NioEventLoopGroup();
        this.workerThreadPool = new NioEventLoopGroup();
    }

    public void start() throws Exception {
        tcpServerStart();
    }

    private void tcpServerStart() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossThreadPool, workerThreadPool)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //TODO: add request handler
                        //ch.pipeline().addLast(new EchoServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        //绑定端口，同步等待绑定成功
        ChannelFuture future = bootstrap.bind(host, port).sync();
        System.out.println("TCP监听开启....");
        //等待服务端监听端口关闭
        //future.channel().closeFuture().sync();
    }

    public void shutdown() {
        workerThreadPool.shutdownGracefully();
        bossThreadPool.shutdownGracefully();
    }
}
