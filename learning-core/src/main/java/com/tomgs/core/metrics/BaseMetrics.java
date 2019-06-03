package com.tomgs.core.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * @author tangzhongyuan
 * @since 2019-05-14 18:34
 **/
public abstract class BaseMetrics {

    public static final MetricRegistry metrics = new MetricRegistry();

    /**
     * 在控制台上打印输出
     */
    public static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
}
