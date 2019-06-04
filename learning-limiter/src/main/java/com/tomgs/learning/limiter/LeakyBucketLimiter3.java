package com.tomgs.learning.limiter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 12:02
 **/
public class LeakyBucketLimiter3 {

    private int numDropsInBucket = 0;
    private Date timeOfLastDropLeak = null;
    private final int _BUCKET_SIZE_IN_DROPS = 20;
    private final long _MS_BETWEEN_DROP_LEAKS = 1000 * 60 * 60; // 1 hour

    public synchronized boolean addDropToBucket() {
        Date now = new Date();
        // first of all, let the bucket leak by the appropriate amount
        if (timeOfLastDropLeak != null) {
            long deltaT = now.getTime() - timeOfLastDropLeak.getTime();
            // note round down as part of integer arithmetic
            long numberToLeak = deltaT / _MS_BETWEEN_DROP_LEAKS;
            if (numberToLeak > 0) { //now go and do the leak
                if (numDropsInBucket <= numberToLeak) {
                    numDropsInBucket = 0;
                } else {
                    numDropsInBucket -= (int) numberToLeak;
                }
                timeOfLastDropLeak = now;
            }
        }

        if (numDropsInBucket < _BUCKET_SIZE_IN_DROPS) {
            numDropsInBucket++;
            return true; // drop added
        }

        return false; // overflow
    }

    public static void main(String[] args) {
        LeakyBucketLimiter3 bucketLimiter = new LeakyBucketLimiter3();
        ExecutorService offerService = Executors.newFixedThreadPool(10);
        //生产线程 10个线程 每秒提交 50个数据  1/0.2s*10=50个
        IntStream.range(0, 10).forEach((i) -> offerService.submit(() -> {
            for (;;) {
                if (bucketLimiter.addDropToBucket()) {
                    // dispatch SMS
                    System.out.println(Thread.currentThread().getId() + " do something...");
                } else {
                    System.out.println(Thread.currentThread().getId() + " discard ...");
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }));
    }

}
