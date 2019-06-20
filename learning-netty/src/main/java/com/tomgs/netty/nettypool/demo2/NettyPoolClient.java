package com.tomgs.netty.nettypool.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;

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

    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;
    public void build() throws Exception {
        strap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(), 2);
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
        final SimpleChannelPool pool = client.poolMap.get(client.addr1);
        for (int i = 0; i < 10; i++) {
            // depending on when you use addr1 or addr2 you will get different pools.
            Future<Channel> f = pool.acquire();
            final int finalI = i;
            f.addListener((FutureListener<Channel>) f1 -> {
                if (f1.isSuccess()) {
                    Channel ch = f1.getNow();
                    ch.attr(CLIENT_CHANNEL_KEY).set(finalI);
                    ch.writeAndFlush(ECHO_REQ);
                    // Release back to pool
                    pool.release(ch);
                }
            });
        }
    }

}
