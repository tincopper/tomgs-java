package com.tomgs.flink.demo.log;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sink for outputting alerts.
 */
@PublicEvolving
@SuppressWarnings("unused")
public class AlertSink implements SinkFunction<String> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory
      .getLogger(org.apache.flink.walkthrough.common.sink.AlertSink.class);

  @Override
  public void invoke(String value, Context context) {
    LOG.info(value);
  }

}
