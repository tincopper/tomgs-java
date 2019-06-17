package com.tomgs.learning.limiter;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * 漏桶限流
 *
 * @author tangzhongyuan
 * @since 2019-06-03 11:08
 **/
public class LeakyBucketLimiter {

    // 定义桶的容量
    private final ConcurrentLinkedQueue<Integer> container = new ConcurrentLinkedQueue<>();

    //
    private static final int BUKET_LIMIT = 500;

    // 定义处理的能力，10req/s
    private final RateLimiter rateLimiter = RateLimiter.create(20d);

    //往桶里面放数据时，确认没有超过桶的最大的容量
    private final Monitor offerMonitor = new Monitor();

    //从桶里消费数据时，桶里必须存在数据
    private Monitor consumerMonitor = new Monitor();

    public void submit(Integer data) {
        if (offerMonitor.enterIf(offerMonitor.newGuard(() -> container.size() < BUKET_LIMIT))) {
            try {
                //说明桶里有容量
                container.offer(data);
                System.out.println(Thread.currentThread() + " submit.." + data + " container size is :[" + container.size() + "]");
            } finally {
                offerMonitor.leave();
            }
        } else {
            //说明桶里没有容量，则拒绝
            //这里时候采用降级策略了。消费速度跟不上产生速度时，而且桶满了，抛出异常
            //或者存入MQ DB等后续处理
            throw new IllegalStateException(Thread.currentThread().getName() + "The bucket is ful..Pls latter can try...");
        }
    }

    public void takeThenConsumer(Consumer<Integer> consumer) {
        if (consumerMonitor.enterIf(consumerMonitor.newGuard(() -> !container.isEmpty()))) {
            try {
                System.out.println(Thread.currentThread() + " waiting " + rateLimiter.acquire());
                final Integer data = container.poll();
                consumer.accept(data);
                /*if (rateLimiter.tryAcquire()) {
                } else {
                    System.out.println("discard ...");
                }*/
            } finally {
                consumerMonitor.leave();
            }
        } else {
            //当桶的消费完后，可以消费那些降级存入MQ或者DB里面的数据
            System.out.println("will consumer Data from MQ...");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final LeakyBucketLimiter bucket = new LeakyBucketLimiter();
        final AtomicInteger DATA_CREATOR = new AtomicInteger(0);

        final MetricRegistry metrics = new MetricRegistry();
        final Meter qps = metrics.meter("QPS");
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(3, TimeUnit.SECONDS);

        ExecutorService offerService = Executors.newFixedThreadPool(10);
        //生产线程 10个线程 每秒提交 50个数据  1/0.2s*10=50个
        IntStream.range(0, 10).forEach((i) -> offerService.submit(() -> {
            for (;;) {
                int data = DATA_CREATOR.incrementAndGet();
                try {
                    bucket.submit(data);
                    //TimeUnit.MILLISECONDS.sleep(200);
                } catch (Exception e) {
                    //对submit时，如果桶满了可能会抛出异常
                    if (e instanceof IllegalStateException) {
                        System.out.println(e.getMessage());
                        //当满了后，生产线程就休眠1分钟
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }));


        //消费线程  采用RateLimiter每秒处理10个  综合的比率是5:1
        ExecutorService consumerService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach((i) -> consumerService.submit(() -> {
            for (;;) {
                bucket.takeThenConsumer(data -> {
                    qps.mark();
                    System.out.println(Thread.currentThread() + " consumer : " + data);
                });
            }
        }));
    }
}
