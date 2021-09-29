/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.registry.jraft.rhea;

import com.tomgs.registry.jraft.rhea.metrics.KVMetrics;
import com.alipay.sofa.jraft.util.FileOutputSignalHandler;
import com.alipay.sofa.jraft.util.MetricReporter;
import com.alipay.sofa.jraft.util.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author jiachun.fjc
 */
public class RheaKVMetricsSignalHandler extends FileOutputSignalHandler {

    private static Logger       LOG       = LoggerFactory.getLogger(RheaKVMetricsSignalHandler.class);

    private static final String DIR       = SystemPropertyUtil.get("rheakv.signal.metrics.dir", "");
    private static final String BASE_NAME = "rheakv_metrics.log";

    @Override
    public void handle(final String signalName) {
        try {
            final File file = getOutputFile(DIR, BASE_NAME);

            LOG.info("Printing rheakv metrics with signal: {} to file: {}.", signalName, file);

            try (final PrintStream out = new PrintStream(new FileOutputStream(file, true))) {
                final MetricReporter reporter = MetricReporter.forRegistry(KVMetrics.metricRegistry()) //
                    .outputTo(out) //
                    .prefixedWith("-- rheakv") //
                    .build();
                reporter.report();
            }
        } catch (final IOException e) {
            LOG.error("Fail to print rheakv metrics.", e);
        }
    }
}
