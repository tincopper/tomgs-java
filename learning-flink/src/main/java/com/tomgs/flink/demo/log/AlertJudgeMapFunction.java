package com.tomgs.flink.demo.log;

import com.tomgs.flink.demo.log.model.AlertEvent;
import org.apache.flink.api.common.functions.RichMapFunction;

/**
 * @author tomgs
 * @since 2021/4/22
 */
public class AlertJudgeMapFunction extends RichMapFunction<AlertEvent, AlertEvent> {

  @Override
  public AlertEvent map(AlertEvent value) throws Exception {
    return value;
  }

}
