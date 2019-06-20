package com.tomgs.es.gateway;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tomgs.es.gateway.common.Props;
import com.tomgs.es.gateway.module.ActionModule;
import com.tomgs.es.gateway.module.CommonModule;
import com.tomgs.es.gateway.module.EsGatewayModule;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.tomgs.es.gateway.core.ServiceProvider.SERVICE_PROVIDER;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 15:30
 **/
public class Bootstrap {

    private static volatile Bootstrap INSTANCE;

    private final CountDownLatch keepAliveLatch = new CountDownLatch(1);
    private final Thread keepAliveThread;

    private Bootstrap() {
        keepAliveThread = new Thread(() -> {
            try {
                keepAliveLatch.await();
            } catch (InterruptedException e) {
                // bail out
            }
        }, "ESGateway-bootstrap-thread");
        keepAliveThread.setDaemon(false);
        // keep this thread alive (non daemon thread) until we shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(keepAliveLatch::countDown));
    }

    public static void main(String[] args) {
        Bootstrap.init();
    }

    static void init() {
        INSTANCE = new Bootstrap();

        final Props props = new Props();
        props.put("server.tcp.host", "127.0.0.1");
        props.put("server.tcp.port", 9301);

        props.put("server.http.host", "127.0.0.1");
        props.put("server.http.port", 9201);

        Injector injector = Guice.createInjector(
                new CommonModule(props),
                //new ActionModule(),
                new EsGatewayModule());

        SERVICE_PROVIDER.setInjector(injector);

        //do somethings ...
        INSTANCE.start();
    }

    private void start() {
        SERVICE_PROVIDER.getInstance(ESGateway.class).start();
        keepAliveThread.start();
    }

    private void stop() throws IOException {
        try {
            //IOUtils.close(INSTANCE.node, INSTANCE.spawner);
            SERVICE_PROVIDER.getInstance(ESGateway.class).stop();
        } finally {
            INSTANCE.keepAliveLatch.countDown();
        }
    }
}
