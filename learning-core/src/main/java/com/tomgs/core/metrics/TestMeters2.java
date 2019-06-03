package com.tomgs.core.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.Random;
import java.util.SortedMap;

/**
 * 统计tps
 * <p>
 * Meters工具会帮助我们统计系统中某一个事件的速率。
 * 比如每秒请求数（TPS），每秒查询数（QPS）等等。
 * 这个指标能反应系统当前的处理能力，帮助我们判断资源是否已经不足。
 * Meters本身是一个自增计数器。
 *
 * @author tangzhongyuan
 * @since 2019-05-14 18:33
 **/
public class TestMeters2 {
    /**
     * 实例化一个Meter
     */
    public static final MetricRegistry qpsMetrics = new MetricRegistry();
    public static final MetricRegistry countMetrics = new MetricRegistry();

    private static final Meter qps = qpsMetrics.meter("QPS");
    private static final Meter count = countMetrics.meter("REQUEST_COUNT");

    public static void handleRequest() {
        final Random random = new Random();
        final int i = random.nextInt(5);
        //qps.mark();
        //count.mark();
        //requests.mark(10);

        qpsMetrics.meter("QPS" + i).mark();
        countMetrics.meter("REQUEST_COUNT" + i).mark(i);
    }

    public static void main(String[] args) throws InterruptedException {
        //reporter.start(3, TimeUnit.SECONDS);
        while (true) {
            handleRequest();
            Thread.sleep(1000);

            //System.out.println(Math.round(qps.getMeanRate()));
            //System.out.println(count.getCount());

            final SortedMap<String, Meter> qpsMap = qpsMetrics.getMeters();
            qpsMap.forEach((key, meter) -> {
                System.out.println(key + " : " + Math.round(meter.getMeanRate()));
            });

            final SortedMap<String, Meter> countMap = countMetrics.getMeters();
            countMap.forEach((key, meter) -> {
                System.out.println(key + " : " + meter.getCount());
            });
        }

    }
}
