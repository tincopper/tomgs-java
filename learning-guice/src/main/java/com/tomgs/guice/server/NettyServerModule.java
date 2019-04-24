package com.tomgs.guice.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.tomgs.guice.server.common.NettyHttpServer;
import com.tomgs.guice.server.common.NettyTcpServer;
import com.tomgs.guice.server.common.Props;
import com.tomgs.guice.server.common.Server;

import javax.inject.Named;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 14:15
 **/
public class NettyServerModule extends AbstractModule {

    public static final String TCP_SERVER = "TCP_SERVER";

    public static final String HTTP_SERVER = "HTTP_SERVER";

    @Override
    protected void configure() {

    }

    @Singleton
    @Provides
    @Named(TCP_SERVER)
    private Server createNettyTcpServer(Props props) {
        String host = props.getString("server.tcp.host");
        int port = props.getInt("server.tcp.port", 9300);
        NettyTcpServer nettyTcpServer = new NettyTcpServer(host, port);
        return nettyTcpServer;
    }

    @Singleton
    @Provides
    @Named(HTTP_SERVER)
    private Server createNettyHttpServer(Props props) {
        String host = props.getString("server.http.host", "127.0.0.1");
        int port = props.getInt("server.http.port", 8080);
        NettyHttpServer nettyHttpServer = new NettyHttpServer(host, port);
        return nettyHttpServer;
    }
}
