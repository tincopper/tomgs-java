package com.tomgs.flink.demo.datastreamapi;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

/**
 * 窗口、时间、水印
 * @author tomgs
 * @since 2021/4/16
 */
public class WindowAndWatermarkDemo {

  final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

  @Test
  public void test() {
    //设置时间属性为 EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
  }

}
