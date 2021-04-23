package com.tomgs.flink.demo.log;

import org.apache.flink.api.common.functions.RichMapFunction;

/**
 * @author tomgs
 * @since 2021/4/22
 */
public class AlertJudgeMapFunction extends RichMapFunction<String, String> {

  @Override
  public String map(String value) throws Exception {
    return value;
  }

}
