package com.tomgs.guice.server;

import com.tomgs.guice.server.common.ServiceProvider;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 15:30
 **/
public class Bootstrap {

    private final CountDownLatch keepAliveLatch = new CountDownLatch(1);
    private final Thread keepAliveThread;

    public Bootstrap() {
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

    void start() {
        //node.start();
        //NettyServer nettyServer = ServiceProvider.SERVICE_PROVIDER.getInstance(NettyServer.class);
        //NettyServer.start(nettyServer);

        keepAliveThread.start();
    }

    void stop() throws IOException {
        try {
            //IOUtils.close(INSTANCE.node, INSTANCE.spawner);
        } finally {
            keepAliveLatch.countDown();
        }
    }
}
