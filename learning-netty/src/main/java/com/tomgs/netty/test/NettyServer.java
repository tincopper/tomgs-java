package com.tomgs.netty.test;

import com.tomgs.netty.test.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.lang.management.ManagementFactory;

/**
 * @author tangzhongyuan
 * @since 2019-04-30 16:45
 **/
public class NettyServer {

    private final String ip;

    private final int port;

    public NettyServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //添加socket处理链
                        ch.pipeline().addLast(
                                new LoggingHandler(LogLevel.INFO),
                                //new ObjectEncoder(),
                                //new ObjectDecoder(1315271800, ClassResolvers.cacheDisabled(null)),
                                new ServerHandler());
                    }
                });
                //.option(ChannelOption.AUTO_READ, false);
                //设置TCP参数
                //.option(ChannelOption.SO_BACKLOG, 1024)
                //.childOption(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口，同步等待绑定成功
        ChannelFuture future = bootstrap.bind(port).addListener(listner ->
                System.out.println("服务" + ip + ":" + port + "启动成功...")).sync();
        //等待服务端监听端口关闭
        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        final NettyServer server = new NettyServer("10.32.4.135", 9001);
        final String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        server.start();
    }
}
