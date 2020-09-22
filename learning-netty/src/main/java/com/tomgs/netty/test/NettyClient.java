package com.tomgs.netty.test;

import com.tomgs.netty.test.handler.ClientHandler3;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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

    public Channel start() throws InterruptedException, ExecutionException {
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
                                new ClientHandler3());
                    }
                });
        ChannelFuture future = bootstrap.connect().sync();
        //future.channel().closeFuture().sync();

        future.get();
        if (!future.isSuccess()) {
            System.out.println("连接失败...");
        }
        return future.channel();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final NettyClient client = new NettyClient("127.0.0.1", 8080);
        for (;;) {
            Channel channel = client.start();
            System.out.println("isActive:" + channel.isActive());
            System.out.println("isOpen:" + channel.isOpen());
            channel.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
            TimeUnit.SECONDS.sleep(5);
        }
    }
}
