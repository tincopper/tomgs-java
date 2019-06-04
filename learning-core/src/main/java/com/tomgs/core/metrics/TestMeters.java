package com.tomgs.core.metrics;

import com.codahale.metrics.Meter;

import java.util.SortedMap;

/**
 * 统计tps
 *
 * Meters工具会帮助我们统计系统中某一个事件的速率。
 * 比如每秒请求数（TPS），每秒查询数（QPS）等等。
 * 这个指标能反应系统当前的处理能力，帮助我们判断资源是否已经不足。
 * Meters本身是一个自增计数器。
 *
 * @author tangzhongyuan
 * @since 2019-05-14 18:33
 **/
public class TestMeters extends BaseMetrics {
    /**
     * 实例化一个Meter
     */
    //private static final Meter requests = metrics.meter(name(TestMeters.class, "request"));
    private static final Meter qps = metrics.meter("QPS");
    private static final Meter count = metrics.meter("REQUEST_COUNT");

    public static void handleRequest() {
        qps.mark();
        count.mark();
        //requests.mark(10);
    }

    public static void main(String[] args) throws InterruptedException {
        //reporter.start(3, TimeUnit.SECONDS);
        while(true){
            handleRequest();
            Thread.sleep(10);

            System.out.println(Math.round(qps.getMeanRate()));
            System.out.println(count.getCount());

            final SortedMap<String, Meter> qpsMap = metrics.getMeters((name, metric) -> "QPS".equals(name));
            qpsMap.forEach((key, meter) -> {
                //System.out.println(key + " : " + meter.getCount());
                System.out.println(key + " : " + Math.round(meter.getMeanRate()));
            });

            final SortedMap<String, Meter> countMap = metrics.getMeters((name, metric) -> "REQUEST_COUNT".equals(name));
            countMap.forEach((key, meter) -> {
                System.out.println(key + " : " + meter.getCount());
                //System.out.println(key + " : " + meter.getMeanRate());
            });
        }

    }
}
