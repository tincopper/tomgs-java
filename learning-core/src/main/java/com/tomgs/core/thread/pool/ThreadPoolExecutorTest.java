package com.tomgs.core.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/3 1.0 
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 5,
                3000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(100));
        //调整线程池大小
        poolExecutor.setCorePoolSize(5);
        poolExecutor.setMaximumPoolSize(15);
        //这个设置为true的话，核心线程在空闲的时候超过设置的超时时间也会进行回收
        poolExecutor.allowCoreThreadTimeOut(true);
    }
}
