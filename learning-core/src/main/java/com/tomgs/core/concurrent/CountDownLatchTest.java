package com.tomgs.core.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author tangzhongyuan
 * @since 2019-08-02 11:30
 **/
public class CountDownLatchTest {
    private final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testCountDown() throws InterruptedException {
        latch.await();
        latch.countDown();
    }
}
