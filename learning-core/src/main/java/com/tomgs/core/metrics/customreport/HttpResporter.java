package com.tomgs.core.metrics.customreport;

import com.codahale.metrics.*;

import java.util.SortedMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自定义report继承ScheduledReporter然后在实现report中实现需要上报的具体逻辑
 *
 * @author tangzhongyuan
 * @since 2019-05-14 19:46
 **/
public class HttpResporter extends ScheduledReporter {

    protected HttpResporter(MetricRegistry registry, String name, MetricFilter filter,
                            TimeUnit rateUnit, TimeUnit durationUnit) {
        super(registry, name, filter, rateUnit, durationUnit);

    }

    protected HttpResporter(MetricRegistry registry, String name, MetricFilter filter,
                            TimeUnit rateUnit, TimeUnit durationUnit, ScheduledExecutorService executor) {
        super(registry, name, filter, rateUnit, durationUnit, executor);

    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {
        //TODO: 上报逻辑


    }
}
