package com.tomgs.learning.limiter;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 漏桶限流
 *
 * @author tangzhongyuan
 * @since 2019-06-03 11:08
 **/
public class LeakyBucketLimiter2 {

    private static final Logger log = LoggerFactory.getLogger(LeakyBucketLimiter2.class);

    private final AtomicInteger buketSize = new AtomicInteger(0);

    private static final int BUKET_LIMIT = 500;

    // 定义处理的能力，qps
    private final RateLimiter rateLimiter;

    //往桶里面放数据时，确认没有超过桶的最大的容量
    private final Monitor offerMonitor = new Monitor();

    //从桶里消费数据时，桶里必须存在数据
    private Monitor consumerMonitor = new Monitor();

    public LeakyBucketLimiter2() {
        rateLimiter = RateLimiter.create(20d);
    }

    public boolean isAllow() {
        final Monitor.Guard guard = new Monitor.Guard(offerMonitor) {
            @Override
            public boolean isSatisfied() {
                return buketSize.get() < BUKET_LIMIT;
            }
        };
        if (offerMonitor.enterIf(guard)) {
            try {
                //说明桶里有容量
                buketSize.incrementAndGet();
                return true;
            } finally {
                offerMonitor.leave();
            }
        }
        return false;
    }

    public void release() {
        final Monitor.Guard guard = new Monitor.Guard(consumerMonitor) {
            @Override
            public boolean isSatisfied() {
                return buketSize.get() > 0;
            }
        };
        if (consumerMonitor.enterIf(guard)) {
            try {
                rateLimiter.acquire();
                buketSize.decrementAndGet();
            } finally {
                consumerMonitor.leave();
            }
        }
    }

    public static void main(String[] args) {
        final LeakyBucketLimiter2 bucket = new LeakyBucketLimiter2();
        final AtomicInteger DATA_CREATOR = new AtomicInteger(0);

        final MetricRegistry metrics = new MetricRegistry();
        final Meter qps = metrics.meter("QPS");
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(3, TimeUnit.SECONDS);

        ExecutorService offerService = Executors.newFixedThreadPool(10);
        //生产线程 10个线程 每秒提交 50个数据  1/0.2s*10=50个
        IntStream.range(0, 10).forEach((i) -> offerService.submit(() -> {
            for (;;) {
                try {
                    if (bucket.isAllow()) {
                        System.out.println(DATA_CREATOR.incrementAndGet());
                        bucket.release();
                    }
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (Exception e) {
                    //对submit时，如果桶满了可能会抛出异常
                    if (e instanceof IllegalStateException) {
                        System.out.println(e.getMessage());
                        //当满了后，生产线程就休眠1分钟
                        try {
                            TimeUnit.SECONDS.sleep(60);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }));

    }

}
