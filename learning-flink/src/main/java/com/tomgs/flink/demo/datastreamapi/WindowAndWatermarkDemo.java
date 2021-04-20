package com.tomgs.flink.demo.datastreamapi;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 窗口、时间、水印
 *
 * @author tomgs
 * @since 2021/4/16
 */
public class WindowAndWatermarkDemo {

  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  DataStream<String> dataStream;

  @Before
  public void before() {
    //设置时间属性为 EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    //设置水印生成时间间隔100ms
    env.getConfig().setAutoWatermarkInterval(100);

    dataStream = env
        //.socketTextStream("127.0.0.1", 9000) // 这个可以通过nc工具实现
        .addSource(new CustomSourceFunction())
        .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<String>() {
          private Long currentTimeStamp = 0L;
          //设置允许乱序时间，0L表示不处理乱序数据
          private Long maxOutOfOrderness = 0L;
//          private Long maxOutOfOrderness = 5000L;

          @Override
          public Watermark getCurrentWatermark() {
            return new Watermark(currentTimeStamp - maxOutOfOrderness);
          }

          @Override
          public long extractTimestamp(String s, long l) {
            String[] arr = s.split(",");
            long timeStamp = Long.parseLong(arr[1]);
            currentTimeStamp = Math.max(timeStamp, currentTimeStamp);
            System.err.println(s + ", EventTime:" + timeStamp + ", watermark:" + (currentTimeStamp - maxOutOfOrderness));
            return timeStamp;
          }
        });
  }

  @Test
  public void test() {
    dataStream.map(new MapFunction<String, Tuple2<String, Long>>() {
      @Override
      public Tuple2<String, Long> map(String s) throws Exception {

        String[] split = s.split(",");
        return new Tuple2<String, Long>(split[0], Long.parseLong(split[1]));
      }
    })
        .keyBy(0)
        .window(TumblingEventTimeWindows.of(Time.seconds(5)))
        .minBy(1)
        .print();
  }

  @After
  public void after() throws Exception {
    env.execute("WaterMark Test Demo");
  }

  static class CustomSourceFunction implements SourceFunction<String> {

    private boolean isRunning = true;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
      while (isRunning) {
        String ele = "flink," + System.currentTimeMillis();
        ctx.collect(ele);
        // 1s 产生1条
        Thread.sleep(1000);
      }
    }

    @Override
    public void cancel() {
      isRunning = false;
    }
  }

}
