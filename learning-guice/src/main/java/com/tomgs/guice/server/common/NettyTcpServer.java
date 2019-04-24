package com.tomgs.guice.server.common;

import com.tomgs.guice.server.handler.EchoServerHandler;
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
public class NettyTcpServer implements Server {

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

    @Override
    public void start() {
        new Thread("tcp_server_main_thread") {
            @Override
            public void run() {
                tcpServerStart();
            }
        }.start();

    }

    private void tcpServerStart() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossThreadPool, workerThreadPool)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            System.out.println("TCP监听开启....");
            //绑定端口，同步等待绑定成功
            ChannelFuture future = bootstrap.bind(host, port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        workerThreadPool.shutdownGracefully();
        bossThreadPool.shutdownGracefully();
    }
}
