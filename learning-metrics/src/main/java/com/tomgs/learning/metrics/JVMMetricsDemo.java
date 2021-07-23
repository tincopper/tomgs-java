package com.tomgs.learning.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import org.junit.Before;
import org.junit.Test;

import static com.tomgs.learning.metrics.MetricsDemo.startReport;
import static com.tomgs.learning.metrics.MetricsDemo.waitSeconds;

/**
 * jvm metrics demo
 *
 * @author tomgs
 * @date 2021/7/23 17:16
 * @since 1.0
 */
public class JVMMetricsDemo {

    private MetricRegistry registry;

    @Before
    public void before() {
        this.registry = new MetricRegistry();
        startReport(registry);
        registerJvmMetrics();
    }

    private void registerJvmMetrics() {
        this.registry.register("MEMORY_Gauge", new MemoryUsageGaugeSet());
        this.registry.register("GC_Gauge", new GarbageCollectorMetricSet());
        this.registry.register("Thread_State_Gauge", new ThreadStatesGaugeSet());
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            waitSeconds(1);
        }
    }

}
