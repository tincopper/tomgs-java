package com.tomgs.netty.nettypool.demo3.test;

import com.tomgs.netty.nettypool.demo3.AbstractChannelPoolMap;
import com.tomgs.netty.nettypool.demo3.ChannelPoolMap;
import com.tomgs.netty.nettypool.demo3.CustomerChannel;
import com.tomgs.netty.nettypool.demo3.CustomerFixedChannelPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:28
 **/
public class NettyPoolClient {
    final EventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap strap = new Bootstrap();

    public static final AttributeKey<Integer> CLIENT_CHANNEL_KEY = AttributeKey.valueOf("netty.client.channel");

    InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8080);
    InetSocketAddress addr2 = new InetSocketAddress("10.0.0.11", 8888);

    ChannelPoolMap<SocketAddress, CustomerFixedChannelPool> poolMap;

    public void build() throws Exception {
        strap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        //.option(ChannelOption.AUTO_READ, false);

        poolMap = new AbstractChannelPoolMap<SocketAddress, CustomerFixedChannelPool>() {
            @Override
            protected CustomerFixedChannelPool newPool(SocketAddress key) {
                return new CustomerFixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(poolMap), 2);
            }
        };
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        NettyPoolClient client = new NettyPoolClient();
        client.build();
        final String ECHO_REQ = "Hello Netty.$_";
        final CustomerFixedChannelPool pool = client.poolMap.get(client.addr1);
        for (int i = 0; i < 10; i++) {
            // depending on when you use addr1 or addr2 you will get different pools.
            final Future<CustomerChannel> f = pool.acquire();
            final int finalI = i;
            f.addListener((FutureListener<CustomerChannel>) f1 -> {
                if (f1.isSuccess()) {
                    final CustomerChannel now = f1.getNow();
                    final Channel ch = now.getChannel();
                    ch.attr(CLIENT_CHANNEL_KEY).set(finalI);
                    ch.writeAndFlush(ECHO_REQ);
                }
            });
        }
    }

}
