package com.tomgs.core.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * @author tangzhongyuan
 * @since 2019-05-14 19:53
 **/
public class TestMetrics {

    private static MetricRegistry metrics = null;
    private static ConsoleReporter reporter = null;

    @Before
    public void setup() {
        metrics = new MetricRegistry();
        /*
         * 在控制台上打印输出
         */
        reporter = ConsoleReporter.forRegistry(metrics).build();
    }

    /**
     * 直方图
     *
     * 直方图是一种非常常见的统计图表，Metrics通过这个Histogram这个度量类型提供了一些方便实时绘制直方图的数据。
     */
    @Test
    public void testHistograms() throws InterruptedException {
        final Histogram histogram = metrics.histogram(name(TestMetrics.class, "random"));
        reporter.start(3, TimeUnit.SECONDS);
        Random rand = new Random();
        while (true) {
            histogram.update((int) (rand.nextDouble() * 100));
            Thread.sleep(100);
        }
    }

    /**
     * 计时器
     *
     * Timer是一个Meter和Histogram的组合。这个度量单位可以比较方便地统计请求的速率和处理时间。
     * 对于接口中调用的延迟等信息的统计就比较方便了。如果发现一个方法的RPS（请求速率）很低，
     * 而且平均的处理时间很长，那么这个方法八成出问题了。
     */
    @Test
    public void testTimers() {
        final Timer request = metrics.timer(name(TestMetrics.class, "request"));
        reporter.start(3, TimeUnit.SECONDS);
        Random random = new Random();
        while (true) {
            final Timer.Context context = request.time();
            try {
                //some operator
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                context.stop();
            }
        }
    }
}
