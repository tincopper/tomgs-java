package com.tomgs.learning.limiter.demo1;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 14:36
 **/
public class LeakyBucket extends RateLimiter {

    private long nextAllowedTime;

    private final long REQUEST_INTERVAL_MILLIS;

    protected LeakyBucket(int maxRequestPerSec) {
        super(maxRequestPerSec);
        REQUEST_INTERVAL_MILLIS = 1000 / maxRequestPerSec;
        nextAllowedTime = System.currentTimeMillis();
    }

    @Override
    boolean allow() {
        long curTime = System.currentTimeMillis();
        synchronized (this) {
            if (curTime >= nextAllowedTime) {
                nextAllowedTime = curTime + REQUEST_INTERVAL_MILLIS;
                return true;
            }
            return false;
        }
    }
}