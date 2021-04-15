package com.tomgs.flink.demo.datastreamapi;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * data stream api
 *
 * @author tomgs
 * @since 2021/4/15
 */
public class DataStreamAggAPIDemo {

  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  DataStreamSource<Tuple3<Integer, Integer, Integer>> items;

  @Before
  public void before() {
    //获取数据源
    List<Tuple3<Integer, Integer, Integer>> data = new ArrayList<>();
    data.add(new Tuple3<>(0, 1, 0));
    data.add(new Tuple3<>(0, 1, 1));
    data.add(new Tuple3<>(0, 2, 2));
    data.add(new Tuple3<>(1, 2, 5));
    data.add(new Tuple3<>(1, 2, 9));
    data.add(new Tuple3<>(1, 2, 11));
    data.add(new Tuple3<>(0, 1, 3));
    data.add(new Tuple3<>(1, 2, 13));
    items = env.fromCollection(data);
  }

  @Test
  public void printOriginData() {
    items.printToErr().setParallelism(1);
  }

  @Test
  public void max() {
    // 这里很多都是函数式接口
    items.keyBy(tuple3 -> tuple3.f0) // 分组
        .max(2) // 求最大值，这里的2是指的tuple的位置
        .printToErr(); // 多了个 (0,1,2)，max只会返回我们制定字段的最大值（即在索引位置为2的最大值，其余索引位置如1则不关心）
  }

  @Test
  public void maxBy() {
    items.keyBy(tuple3 -> tuple3.f0) // 分组
        .maxBy(2) // 求最大值，这里的2是指的tuple的位置
        .printToErr(); // 直接按照输入元素顺序输出，因为这个没有用窗口进行数据统计，所以只是统计一条，在实际生产环境中应该尽量避免在一个无限流上使用 Aggregations
  }

  @Test
  public void reduce() {
    items.keyBy(tuple3 -> tuple3.f0)
        .reduce((t1, t2) -> {
          Tuple3<Integer, Integer, Integer> newTuple3 = new Tuple3<>();
          int sum = (int) t1.getField(2) + (int) t2.getField(2);
          newTuple3.setFields(0, 0, sum);
          return newTuple3;
        })
        .printToErr()
        .setParallelism(1);
  }

  @Test
  public void sum() {
    items.keyBy(tuple3 -> tuple3.f0)
        .sum(2)
        .printToErr();
  }

  @Test
  public void process() {
    items.keyBy(tuple3 -> tuple3.f0)
        .process(new KeyedProcessFunction<Integer, Tuple3<Integer, Integer, Integer>, Integer>() {
          @Override
          public void processElement(Tuple3<Integer, Integer, Integer> value, Context ctx, Collector<Integer> out) {
            out.collect(value.f2 * 2);
          }
        })
        .printToErr();
  }

  @After
  public void after() throws Exception {
    env.execute("data stream demo");
  }

}
