package com.tomgs.netty.test;

import com.tomgs.netty.test.handler.ClientHandler;
import com.tomgs.netty.test.handler.ClientHandler2;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @author tangzhongyuan
 * @since 2019-04-30 16:45
 **/
public class NettyClient {

    private final String host;

    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        final Bootstrap bootstrap = new Bootstrap();
        final EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new LoggingHandler(LogLevel.INFO),
                                //new ObjectEncoder(),
                                //new ObjectDecoder(1315271800, ClassResolvers.cacheDisabled(null)),
                                new ClientHandler());
                    }
                });
        ChannelFuture f = bootstrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        final NettyClient client = new NettyClient("127.0.0.1", 9001);
        client.start();
    }
}
