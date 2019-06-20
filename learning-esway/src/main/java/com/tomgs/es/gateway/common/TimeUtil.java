package com.tomgs.es.gateway.common;

import java.util.concurrent.TimeUnit;

/**
 * cache current time millis util
 *
 * @author tangzhongyuan
 * @since 2019-06-14 15:40
 **/
public final class TimeUtil {
    private static volatile long currentTimeMillis;

    static {
        currentTimeMillis = System.currentTimeMillis();
        Thread daemon = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    currentTimeMillis = System.currentTimeMillis();
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (Throwable ignore) {

                    }
                }
            }
        });
        daemon.setDaemon(true);
        daemon.setName("esp-time-tick-thread");
        daemon.start();
    }

    public static long currentTimeMillis() {
        return currentTimeMillis;
    }
}
