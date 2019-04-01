package com.tomgs.core.queue;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  
 *
 * @author tomgs
 * @version 2019/4/2 1.0
 */
public class TaskExecutor {

    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueue workQueue = new PriorityBlockingQueue(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10,
                30000, TimeUnit.SECONDS, workQueue);

        TaskDemo demo1 = new TaskDemo("1", Priority.DEFAULT);
        TaskDemo demo2 = new TaskDemo("2", Priority.LOW);
        TaskDemo demo3 = new TaskDemo("3", Priority.HIGH);
        TaskDemo demo4 = new TaskDemo("4", Priority.NOW);

        executor.execute(demo1);
        executor.execute(demo2);
        executor.execute(demo3);
        executor.execute(demo4);

        Thread.sleep(5 * 1000);

        executor.shutdown();
    }
}
