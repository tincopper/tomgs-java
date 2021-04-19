package com.tomgs.flink.demo.datastreamapi;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.junit.Before;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/4/19
 */
public class SideOutPutDemo {

  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  DataStreamSource<Tuple3<Integer, Integer, Integer>> items;

  @Before
  public void before() {
    //获取数据源
    List<Tuple3<Integer, Integer, Integer>> data = new ArrayList<>();
    data.add(new Tuple3<>(0, 1, 0));
    data.add(new Tuple3<>(0, 1, 1));
    data.add(new Tuple3<>(0, 2, 2));
    data.add(new Tuple3<>(0, 1, 3));
    data.add(new Tuple3<>(1, 2, 5));
    data.add(new Tuple3<>(1, 2, 9));
    data.add(new Tuple3<>(1, 2, 11));
    data.add(new Tuple3<>(1, 2, 13));

    items = env.fromCollection(data);
  }

  /**
   * Filter 的弊端是显而易见的，为了得到我们需要的流数据，需要多次遍历原始流，这样无形中浪费了我们集群的资源。
   */
  @Test
  public void filterStream() throws Exception {
    SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> zeroStream = items
        .filter((FilterFunction<Tuple3<Integer, Integer, Integer>>) value -> value.f0 == 0);
    SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> oneStream = items
        .filter((FilterFunction<Tuple3<Integer, Integer, Integer>>) value -> value.f0 == 1);

    zeroStream.print();
    oneStream.printToErr();

    //打印结果
    String jobName = "user defined streaming source";
    env.execute(jobName);
  }

  /**
   * 使用 split 算子切分过的流，是不能进行二次切分的，假如把上述切分出来的 zeroStream 和 oneStream 流再次调用 split 切分，控制台会抛出以下异常。
   * Exception in thread "main" java.lang.IllegalStateException: Consecutive multiple splits are not supported. Splits are deprecated. Please use side-outputs.
   */
  @Test
  public void splitStream() throws Exception {
    SplitStream<Tuple3<Integer, Integer, Integer>> splitStream = items.split(new OutputSelector<Tuple3<Integer, Integer, Integer>>() {
      @Override
      public Iterable<String> select(Tuple3<Integer, Integer, Integer> value) {
        List<String> tags = new ArrayList<>();
        if (value.f0 == 0) {
          tags.add("zeroStream");
        } else if (value.f0 == 1) {
          tags.add("oneStream");
        }
        return tags;
      }
    });

    splitStream.select("zeroStream").print();
    splitStream.select("oneStream").printToErr();

    //打印结果
    String jobName = "user defined streaming source";
    env.execute(jobName);
  }

  /**
   * SideOutPut 是 Flink 框架为我们提供的最新的也是最为推荐的分流方法
   * Flink 最新提供的 SideOutPut 方式拆分流是可以多次进行拆分的，无需担心会爆出异常。
   */
  @Test
  public void processSideOutPut() throws Exception {
    OutputTag<Tuple3<Integer,Integer,Integer>> zeroStream = new OutputTag<Tuple3<Integer,Integer,Integer>>("zeroStream") {};
    OutputTag<Tuple3<Integer,Integer,Integer>> oneStream = new OutputTag<Tuple3<Integer,Integer,Integer>>("oneStream") {};


    SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> processStream= items.process(new ProcessFunction<Tuple3<Integer, Integer, Integer>, Tuple3<Integer, Integer, Integer>>() {
      @Override
      public void processElement(Tuple3<Integer, Integer, Integer> value, Context ctx, Collector<Tuple3<Integer, Integer, Integer>> out) throws Exception {
        if (value.f0 == 0) {
          ctx.output(zeroStream, value);
        } else if (value.f0 == 1) {
          ctx.output(oneStream, value);
        }
      }
    });

    DataStream<Tuple3<Integer, Integer, Integer>> zeroSideOutput = processStream.getSideOutput(zeroStream);
    DataStream<Tuple3<Integer, Integer, Integer>> oneSideOutput = processStream.getSideOutput(oneStream);

    zeroSideOutput.print();
    oneSideOutput.printToErr();

    //打印结果
    String jobName = "user defined streaming source";
    env.execute(jobName);
  }

}
