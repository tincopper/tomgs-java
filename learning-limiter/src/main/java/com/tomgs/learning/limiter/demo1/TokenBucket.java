package com.tomgs.learning.limiter.demo1;

import java.util.concurrent.TimeUnit;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 14:51
 **/
public class TokenBucket extends RateLimiter {

    private int tokens;

    public TokenBucket(int maxRequestsPerSec) {
        super(maxRequestsPerSec);
        this.tokens = maxRequestsPerSec;
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refillTokens(maxRequestsPerSec);
            }
        }).start();
    }

    @Override
    public boolean allow() {
        synchronized (this) {
            if (tokens == 0) {
                return false;
            }
            tokens--;
            return true;
        }
    }

    private void refillTokens(int cnt) {
        synchronized (this) {
            tokens = Math.min(tokens + cnt, maxRequestPerSec);
            notifyAll();
        }
    }
}
