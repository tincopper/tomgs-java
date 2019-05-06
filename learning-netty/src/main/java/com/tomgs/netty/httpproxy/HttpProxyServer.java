package com.tomgs.netty.httpproxy;

import com.tomgs.netty.httpproxy.handler.HttpProxyFrontendHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 针对http的请求进行代理
 *
 * @author tangzhongyuan
 * @since 2019-05-06 16:48
 **/
public class HttpProxyServer {

    public void start(String host, int port) {
        EventLoopGroup bossThreadPool = new NioEventLoopGroup();
        EventLoopGroup workerThreadPool = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossThreadPool, workerThreadPool);
            b.channel(NioServerSocketChannel.class);
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel e) throws Exception {
                    e.pipeline().addLast("http-codec", new HttpServerCodec());
                    e.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                    e.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    e.pipeline().addLast("handler", new HttpProxyFrontendHandler());
                }
            });
            ChannelFuture future = b.bind(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("HTTP监听 [" + host + ":" + port + "] 开启....");
                    }
                }
            }).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossThreadPool.shutdownGracefully();
            workerThreadPool.shutdownGracefully();
        }
    }


}
