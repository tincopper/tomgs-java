package com.tomgs.es.gateway.transport.tcp.netty4;

import com.tomgs.es.gateway.transport.tcp.netty4.custom.ProxyFrontendHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.elasticsearch.common.component.AbstractLifecycleComponent;

import java.io.IOException;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 14:21
 **/
public class NettyTcpServer extends AbstractLifecycleComponent {

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
    protected void doStart() {
        try {
            tcpServerStart();
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

    private void tcpServerStart() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossThreadPool, workerThreadPool)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("logging", new ESLoggingHandler());
                        ch.pipeline().addLast("size", new Netty4SizeHeaderFrameDecoder());
                        ch.pipeline().addLast("dispatcher", new ProxyFrontendHandler());
                    }
                })
                .childOption(ChannelOption.AUTO_READ, false);
        //绑定端口，同步等待绑定成功
        ChannelFuture future = bootstrap.bind(host, port).addListener(listner ->
                System.out.println("ES_WAY_TCP服务" + host + ":" + port + "启动成功...")).sync();
        //等待服务端监听端口关闭
        //future.channel().closeFuture().sync();
    }

}
