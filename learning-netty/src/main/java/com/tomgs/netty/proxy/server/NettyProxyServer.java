package com.tomgs.netty.proxy.server;

import com.tomgs.netty.proxy.server.handler.ProxyFrontendHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 14:22
 **/
public class NettyProxyServer {

    private final String ip;

    private final int port;

    public NettyProxyServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //添加socket处理链
                        ch.pipeline().addLast(
                                new LoggingHandler(LogLevel.INFO),
                                //new ObjectEncoder(),
                                //new ObjectDecoder(1315271800, ClassResolvers.cacheDisabled(null)),
                                new ProxyFrontendHandler());
                    }
                })
                //这里设置都是childOption不是option
                .childOption(ChannelOption.AUTO_READ, false);
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
        final NettyProxyServer server = new NettyProxyServer("10.18.4.23", 9001);
        server.start();
    }
}
