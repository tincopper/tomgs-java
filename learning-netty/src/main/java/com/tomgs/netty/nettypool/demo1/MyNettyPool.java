package com.tomgs.netty.nettypool.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;

/**
 * 使用netty自带的连接池
 *
 * @author tangzhongyuan
 * @since 2019-06-17 14:16
 **/
public class MyNettyPool {

    //key为目标host，value为目标host的连接池
    public ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap = null;

    public MyNettyPool() {
        init();
    }

    public void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        final Bootstrap cb = new Bootstrap();
        cb.group(group).channel(NioSocketChannel.class);
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(cb.remoteAddress(key), new ChannelPoolHandler() {
                    public void channelReleased(Channel ch) throws Exception {
                        System.out.println("22");
                    }

                    public void channelAcquired(Channel ch) throws Exception {
                        System.out.println("33");
                    }

                    public void channelCreated(Channel ch) throws Exception {
                        //可以在此绑定channel的handler
                        ch.pipeline().addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024))
                                .addLast(new HttpBackendHandler());
                    }
                }, 20);//单个host连接池大小
            }
        };

    }

    public void getHttpClient(InetSocketAddress address, final FullHttpRequest msg) {
        if (address == null) {
            throw new RuntimeException("InetSocketAddress can not be null");
        }

        final FixedChannelPool pool = this.poolMap.get(address);
        Future<Channel> future = pool.acquire();
        future.addListener(new FutureListener<Channel>() {

            public void operationComplete(Future<Channel> f) {
                if (f.isSuccess()) {
                    Channel ch = f.getNow();
                    ChannelFuture lastWriteFuture = null;

                    lastWriteFuture = ch.writeAndFlush(msg);

                    // Wait until all messages are flushed before closing the channel.
                    if (lastWriteFuture != null) {

                        try {
                            lastWriteFuture.sync();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    pool.release(ch);
                }
            }
        });
    }


    public void send(InetSocketAddress address, final Object msg) {
        //封装请求数据

        //从连接池中获取连接
        final FixedChannelPool pool = poolMap.get(address);
        //申请连接，没有申请到或者网络断开，返回null
        Future<Channel> future = pool.acquire();
        future.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> future) throws Exception {
                //给服务端发送数据
                Channel channel = future.getNow();
                channel.writeAndFlush(msg);

                System.out.println(channel.id());
                // 连接放回连接池，这里一定记得放回去
                pool.release(channel);
            }
        });

        //获取服务端返回的数据
    }
}
