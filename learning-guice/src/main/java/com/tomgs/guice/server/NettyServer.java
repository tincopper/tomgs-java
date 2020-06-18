package com.tomgs.guice.server;

import static com.tomgs.guice.server.NettyServerModule.HTTP_SERVER;
import static com.tomgs.guice.server.NettyServerModule.TCP_SERVER;
import static com.tomgs.guice.server.common.ServiceProvider.SERVICE_PROVIDER;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.tomgs.guice.server.common.Props;
import com.tomgs.guice.server.common.Server;
import javax.inject.Named;

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
    private final Bootstrap bootstrap;

    @Inject
    public NettyServer(@Named(TCP_SERVER) final Server tcpServer,
                       @Named(HTTP_SERVER) final Server httpServer, final Bootstrap bootstrap) {
        this.tcpServer = tcpServer;
        this.httpServer = httpServer;
        this.bootstrap = bootstrap;
    }

    public static void main(String[] args) {

        //final Props props = AzkabanServer.loadProps(args);
        final Props props = new Props();
        props.put("server.tcp.host", "127.0.0.1");
        props.put("server.tcp.port", 9301);

        props.put("server.http.host", "127.0.0.1");
        props.put("server.http.port", 8080);

        Injector injector = Guice.createInjector(
                new NettyServerCommonModule(props),
                new NettyServerModule());
        SERVICE_PROVIDER.setInjector(injector);

        try {
            NettyServer server = injector.getInstance(NettyServer.class);
            server.launch(injector.getInstance(NettyServer.class));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //private static final CountDownLatch keepAliveLatch = new CountDownLatch(1);
    private void launch(final NettyServer nettyServer) throws InterruptedException {

        //启动方式1
        //start(nettyServer);
        // ...
        //Runtime.getRuntime().addShutdownHook(new Thread(keepAliveLatch::countDown));
        //keepAliveLatch.await();

        //启动方式2
        // 这个server可以放到bootstrap里面去
        start(nettyServer);
        bootstrap.start();
    }

    void start(final NettyServer nettyServer) {
        try {
            nettyServer.tcpServer.start();
            nettyServer.httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
