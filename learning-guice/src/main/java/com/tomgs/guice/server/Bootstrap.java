package com.tomgs.guice.server;

import com.tomgs.guice.server.common.ServiceProvider;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 15:30
 **/
public class Bootstrap {

    private static volatile Bootstrap INSTANCE;

    private final CountDownLatch keepAliveLatch = new CountDownLatch(1);
    private final Thread keepAliveThread;

    private Bootstrap() {
        keepAliveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    keepAliveLatch.await();
                } catch (InterruptedException e) {
                    // bail out
                }
            }
        }, "bootstrap_thread");
        keepAliveThread.setDaemon(false);
        // keep this thread alive (non daemon thread) until we shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                keepAliveLatch.countDown();
            }
        });
    }

    static void init() {
        INSTANCE = new Bootstrap();
        //do somethings ...
        INSTANCE.start();
    }

    private void start() {
        //node.start();
        NettyServer nettyServer = ServiceProvider.SERVICE_PROVIDER.getInstance(NettyServer.class);
        NettyServer.start(nettyServer);

        keepAliveThread.start();
    }

    static void stop() throws IOException {
        try {
            //IOUtils.close(INSTANCE.node, INSTANCE.spawner);
        } finally {
            INSTANCE.keepAliveLatch.countDown();
        }
    }
}
