package com.tomgs.es.gateway.transport.http.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.elasticsearch.common.component.AbstractLifecycleComponent;

import java.io.IOException;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 14:22
 **/
public class NettyHttpServer extends AbstractLifecycleComponent {

    private final String host;
    private final int port;

    private final EventLoopGroup bossThreadPool;
    private final EventLoopGroup workerThreadPool;

    public NettyHttpServer(final String host, final int port) {
        this.host = host;
        this.port = port;

        this.bossThreadPool = new NioEventLoopGroup();
        this.workerThreadPool = new NioEventLoopGroup();
    }

    @Override
    protected void doStart() {
        try {
            httpServerStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() throws IOException {
        bossThreadPool.shutdownGracefully();
        workerThreadPool.shutdownGracefully();
    }

    private void httpServerStart() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossThreadPool, workerThreadPool);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel e) throws Exception {
                e.pipeline().addLast("http-codec", new HttpServerCodec());
                e.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                e.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                e.pipeline().addLast("handler", new HttpRequestHandler());
            }
        });
        ChannelFuture future = b.bind(host, port).sync();
        System.out.println("HTTP监听开启....");
        //future.channel().closeFuture().sync();
    }

}
