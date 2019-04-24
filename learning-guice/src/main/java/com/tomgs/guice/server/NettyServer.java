package com.tomgs.guice.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tomgs.guice.server.common.Props;
import com.tomgs.guice.server.common.Server;

import javax.inject.Named;

import static com.tomgs.guice.server.NettyServerModule.HTTP_SERVER;
import static com.tomgs.guice.server.NettyServerModule.TCP_SERVER;
import static com.tomgs.guice.server.common.ServiceProvider.SERVICE_PROVIDER;

/**
 * netty server start
 *
 * @author tangzhongyuan
 * @create 2019-04-22 14:15
 **/
@Singleton
public class NettyServer {

    private final Server tcpServer;
    private final Server httpServer;

    @Inject
    public NettyServer(@Named(TCP_SERVER) final Server tcpServer,
                       @Named(HTTP_SERVER) final Server httpServer) {
        this.tcpServer = tcpServer;
        this.httpServer = httpServer;
    }

    public static void main(String[] args) {

        //final Props props = AzkabanServer.loadProps(args);
        final Props props = new Props();
        props.put("server.tcp.host", "10.18.4.23");
        props.put("server.tcp.port", 9300);

        props.put("server.http.host", "10.18.4.23");
        props.put("server.http.port", 8080);

        Injector injector = Guice.createInjector(
                new NettyServerCommonModule(props),
                new NettyServerModule());
        SERVICE_PROVIDER.setInjector(injector);

        launch(injector.getInstance(NettyServer.class));
    }

    private static void launch(final NettyServer nettyServer) {
        //这里需要开启两个线程分别启动tcp和http服务
        nettyServer.tcpServer.start();
        nettyServer.httpServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // ...
            }
        });
    }
}
