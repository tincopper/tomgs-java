package com.tomgs.flink.demo.datastreamapi;

import com.tomgs.flink.demo.datastreamapi.MyStreamingSource.Item;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichAggregateFunction;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
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
  public void filter1() {
    SingleOutputStreamOperator<Item> filter = items.filter(new RichFilterFunction<Item>() {

      @Override
      public void open(Configuration parameters) throws Exception {
        super.open(parameters);
      }

      @Override
      public boolean filter(Item value) throws Exception {
        return false;
      }

    });
    filter.printToErr();
  }

  @Test
  public void apply() {
    items.keyBy(Item::getName)
            //3s钟一个窗口
        .timeWindow(Time.seconds(3))
        .apply(new WindowFunction<Item, String, String, TimeWindow>() {
          @Override
          public void apply(String key, TimeWindow window, Iterable<Item> input, Collector<String> out) throws Exception {
            input.forEach(item -> {
              out.collect(item.getName());
            });
            out.collect("----");
          }
        })
        .printToErr();
  }

  @Test
  public void process() {
    items.keyBy(Item::getName)
            .timeWindow(Time.seconds(3))
            .process(new ProcessWindowFunction<Item, String, String, TimeWindow>() {
              @Override
              public void process(String key, Context context, Iterable<Item> input, Collector<String> out) throws Exception {
                input.forEach(item -> {
                  out.collect(item.getName());
                });
                out.collect("----");
              }
            })
            .printToErr();
  }

  /**
   * 对每5次的item id进行求平均值
   */
  @Test
  public void aggregate() {
    items.keyBy(Item::getName)
            .countWindow(2)
            .aggregate(new IdAverageAggregate())
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

  /**
   * 输入item对id进行求和，然后求平均值
   * Tuple3<String, Integer, Integer> name idSum idCount
   * Tuple3<String, Integer, Double> name idSum idAvg
   */
  static class IdAverageAggregate implements AggregateFunction<Item, Tuple3<String, Integer, Integer>, Tuple3<String, Integer, Double>> {


    @Override
    public Tuple3<String, Integer, Integer> createAccumulator() {
      // 创建一个新的累加器
      return new Tuple3<>("", 0, 0);
    }

    @Override
    public Tuple3<String, Integer, Integer> add(Item value, Tuple3<String, Integer, Integer> accumulator) {
      // 累加
      return new Tuple3<>(value.getName(), accumulator.f1 + value.getId(), accumulator.f2 + 1);
    }

    @Override
    public Tuple3<String, Integer, Double> getResult(Tuple3<String, Integer, Integer> accumulator) {
      // 获取结果
      return new Tuple3<>(accumulator.f0, accumulator.f1, (double) accumulator.f1 / accumulator.f2);
    }

    @Override
    public Tuple3<String, Integer, Integer> merge(Tuple3<String, Integer, Integer> a, Tuple3<String, Integer, Integer> b) {
      // 合并两个累加器，返回一个具有合并状态的累加器（sessionwindow可用）
      return null;
    }

  }

}
