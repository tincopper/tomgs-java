package com.tomgs.core.unsafe;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupportTest
 *
 * @author tomgs
 * @since 2021/11/2
 */
public class LockSupportTest {

    @Test
    public void testParkAndUnPark() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("step 1.");
            LockSupport.park();
            System.out.println("step 2.");
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            System.out.println("release thread.");
            LockSupport.unpark(thread);
        });
        thread1.start();

        Thread.sleep(5000);
    }

}
