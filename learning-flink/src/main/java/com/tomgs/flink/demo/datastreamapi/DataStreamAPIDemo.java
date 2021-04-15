package com.tomgs.flink.demo.datastreamapi;

import com.tomgs.flink.demo.datastreamapi.MyStreamingSource.Item;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.After;
import org.junit.Test;

/**
 * data stream api
 *
 * @author tomgs
 * @since 2021/4/15
 */
public class DataStreamAPIDemo {

  private StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  private DataStreamSource<Item> items = env.addSource(new MyStreamingSource()).setParallelism(1);

  @Test
  public void printOriginData() {
    // Item{name='name83', id=83}
    // Item{name='name75', id=75}
    // ...
    items.printToErr().setParallelism(1);
  }

  @Test
  public void map() {
    // map function
    // 转换：item -> item.name
    //SingleOutputStreamOperator<String> mapItems = items.map(Item::getName);
    SingleOutputStreamOperator<String> mapItems = items.map(new MyMapFunction());
    // name21
    // name4
    // name11
    // ...
    mapItems.printToErr().setParallelism(1);
  }

  @Test
  public void flatMap() {
    SingleOutputStreamOperator<String> flatMap = items.flatMap(new RichFlatMapFunction<Item, String>() {
      @Override
      public void flatMap(Item value, Collector<String> out) {
        out.collect(value.getName());
      }
    });

    flatMap.printToErr();
  }

  @Test
  public void filter() {
    SingleOutputStreamOperator<Item> filter = items.filter(item -> item.getId() % 2 == 0);
    filter.printToErr();
  }

  @Test
  public void apply() {
    items.keyBy(Item::getName)
        .timeWindow(Time.seconds(3))
        .apply(new WindowFunction<Item, String, String, TimeWindow>() {
          @Override
          public void apply(String s, TimeWindow window, Iterable<Item> input,
              Collector<String> out) throws Exception {
            
          }
        })
        .printToErr();
  }

  @After
  public void after() throws Exception {
    env.execute("data stream demo");
  }

  /**
   * 自定义map function
   */
  static class MyMapFunction extends RichMapFunction<Item, String> {
    @Override
    public String map(MyStreamingSource.Item item) {
      return item.getName();
    }
  }

}
