package com.tomgs.learning.metrics;

import com.codahale.metrics.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * metrics demo
 *
 * @author tomgs
 * @date 2021/7/23 11:10
 * @since 1.0
 */
public class MetricsDemo {

    private static final MetricRegistry metrics = new MetricRegistry();

    @Before
    public void before() {
        startReport(metrics);
    }

    static void startReport(MetricRegistry metrics) {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    @After
    public void after() {
        //waitSeconds(5);
    }

    /**
     * 记录请求QPS
     * <p>
     * 　Meters工具会帮助我们统计系统中某一个事件的速率。比如每秒请求数（TPS），每秒查询数（QPS）等等。这个指标能反应系统当前的处理能力，帮助我们判断资源是否已经不足。Meters本身是一个自增计数器。
     */
    @Test
    public void testMeter() {
        Meter requests = metrics.meter("requests");
        for (int i = 0; i < 100; i++) {
            requests.mark();
        }
    }

    /**
     * 度量（仪表）
     * Gauge代表一个度量的即时值。 当你开汽车的时候， 当前速度是Gauge值。 你测体温的时候， 体温计的刻度是一个Gauge值。 当你的程序运行的时候， 内存使用量和CPU占用率都可以通过Gauge值来度量。
     * <p>
     * 我们可以通过Gauges完成自定义的度量类型。比方说很简单的，我们想看我们缓存里面的数据大小，就可以自己定义一个Gauges，统计队列的大小等。
     * 比方说统计运行时队列的size，（原生的size方法的复杂度为O(n)，队列数据一大比较耗时，这点需要考虑进去）
     */
    @Test
    public void testGauges() {
        QueueManager queueManager = new QueueManager(metrics, "queue");
        for (int i = 0; i < 4; i++) {
            queueManager.queue.add("" + i);
            waitSeconds(1);
        }
    }

    @Test
    public void testGauges2() {
        Map<String, String> cache = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            cache.put("" + i, "" + i);
        }
        Meter meter = metrics.meter("cache_count");
        Timer timer = metrics.timer("cache_time");

        waitSeconds(3);

        for (int i = 0; i < 100; i++) {
            Timer.Context context = timer.time();
            try {
                String result = cache.get("" + i * 2);
                if (result != null) {
                    meter.mark();
                }
            } finally {
                context.stop();
            }
        }

        waitSeconds(1);

        CacheHitRatio cacheHitRatio = new CacheHitRatio(meter, timer);
        System.out.println("CacheHitRatio=========>" + cacheHitRatio.getRatio());
        System.out.println("CacheHitPercent=========>" + cacheHitRatio.getValue()); // 接近50%
    }

    /**
     * 计数器
     * Counter本质就是一个AtomicLong实例，可以通过增加和减少相应的值。
     * 如衡量队列中的待处理作业
     * <p>
     * public void addJob(Job job) {
     * pendingJobs.inc();
     * queue.offer(job);
     * }
     * <p>
     * public Job takeJob() {
     * pendingJobs.dec();
     * return queue.take();
     * }
     */
    @Test
    public void testCounter() {
        Counter pendingJobs = metrics.counter("pending-jobs");
        for (int i = 0; i < 1000; i++) {
            pendingJobs.inc();
            pendingJobs.inc(1);
        }
        waitSeconds(1);
        for (int i = 0; i < 10; i++) {
            pendingJobs.dec();
            waitSeconds(1);
            pendingJobs.dec(1);
        }
    }

    /**
     * 直方图
     * 直方图是一种非常常见的统计图表，Metrics通过这个Histogram这个度量类型提供了一些方便实时绘制直方图的数据。比如我们需要统计某个方法的网络流量，
     * 通过Histogram就非常的方便。Histogram为我们提供了最大值，最小值和平均值等数据外还提供百分位数75th, 90th, 95th, 98th, 99th, and 99.9th percentiles，
     * 利用这些数据，我们就可以开始绘制自定义的直方图了。
     */
    @Test
    public void testHistogram() {
        Histogram responseSizes = metrics.histogram(MetricRegistry.name(RequestHandler.class, "response-size"));
        RequestHandler handler = new RequestHandler(responseSizes);
        for (int i = 0; i < 1000; i++) {
            handler.handleRequestHistogram("request", "response");
        }
    }

    /**
     * 计时器
     * 计时器既能测量某段代码被调用的速度，又能测量其持续时间的分布。
     */
    @Test
    public void testTimers() {
        Timer responses = metrics.timer(MetricRegistry.name(RequestHandler.class, "responses"));
        RequestHandler handler = new RequestHandler(responses);
        for (int i = 0; i < 3; i++) {
            handler.handleRequestTimers("request", "response");
        }
    }

    static void waitSeconds(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    static class RequestHandler {

        private final Histogram responseSizes;

        private final Timer responses;

        public RequestHandler(final Histogram responseSizes) {
            this.responseSizes = responseSizes;
            this.responses = null;
        }

        public RequestHandler(Timer responses) {
            this.responseSizes = null;
            this.responses = responses;
        }

        public void handleRequestHistogram(Object request, Object response) {
            // etc response.getContent().length instead of random
            assert responseSizes != null;
            responseSizes.update(new Random().nextInt(10));
        }

        public void handleRequestTimers(Object request, Object response) {
            // etc response.getContent().length instead of random
            assert responses != null;
            Timer.Context context = responses.time();
            try {
                // do something
                waitSeconds(new Random().nextInt(3));
            } finally {
                context.stop();
            }
        }
    }

    static class QueueManager {

        private final Queue<String> queue;

        public QueueManager(MetricRegistry metrics, String name) {
            this.queue = new ArrayBlockingQueue<>(16);
            metrics.register(MetricRegistry.name(QueueManager.class, name, "size"),
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return queue.size();
                        }
                    });
        }
    }

    /**
     * 缓存命中率统计
     */
    static class CacheHitRatio extends RatioGauge {

        private final Meter hits;
        private final Timer calls;

        public CacheHitRatio(Meter hits, Timer calls) {
            this.hits = hits;
            this.calls = calls;
        }

        @Override
        protected Ratio getRatio() {
            // 秒级别
            return Ratio.of(hits.getMeanRate(), calls.getMeanRate());
            // 分钟级别
            //return Ratio.of(hits.getOneMinuteRate(), calls.getOneMinuteRate());
        }

    }
}