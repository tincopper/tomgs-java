package com.tomgs.netty.nettypool.demo1;

import java.net.InetSocketAddress;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 15:10
 **/
public class NettyHttpClientPoolTest {

    public static void main(String[] args) {
        MyNettyPool pool = new MyNettyPool();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        String request = "test";
        pool.send(address, request);
    }

}
