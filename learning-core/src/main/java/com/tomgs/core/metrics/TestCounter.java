package com.tomgs.core.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * 计数器
 *
 * Counter的本质就是一个AtomicLong实例，可以增加或者减少值，可以用它来统计队列中Job的总数。
 *
 * @author tangzhongyuan
 * @since 2019-05-14 18:20
 **/
public class TestCounter {

    private static final MetricRegistry metrics = new MetricRegistry();

    /**
     * 在控制台上打印输出
     */
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();

    /**
     * 实例化一个counter,同样可以通过如下方式进行实例化再注册进去
     * pendingJobs = new Counter();
     * metrics.register(MetricRegistry.name(TestCounter.class, "pending-jobs"), pendingJobs);
     */
    private static Counter pendingJobs = metrics.counter(name(TestCounter.class, "pedding-jobs"));

    private static Queue<String> queue = new LinkedList<String>();

    public static void add(String str) {
        pendingJobs.inc();
        //pendingJobs.inc(10);
        queue.offer(str);
    }

    public String take() {
        pendingJobs.dec();
        return queue.poll();
    }

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);
        while(true){
            add("1");
            Thread.sleep(1000);
        }

    }
}
