package com.tomgs.learning.limiter.demo1;

/**
 * https://hechao.li/2018/06/25/Rate-Limiter-Part1/
 *
 * @author tangzhongyuan
 * @since 2019-06-04 14:35
 **/
public abstract class RateLimiter {

    protected final int maxRequestPerSec;

    protected RateLimiter(int maxRequestPerSec) {
        this.maxRequestPerSec = maxRequestPerSec;
    }

    abstract boolean allow();
}
