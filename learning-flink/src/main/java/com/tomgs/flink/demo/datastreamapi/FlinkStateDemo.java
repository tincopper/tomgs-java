package com.tomgs.flink.demo.datastreamapi;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * https://kaiwu.lagou.com/course/courseInfo.htm?sid=&courseId=81&lagoufrom=noapp&sharetype=copy#/detail/pc?id=2044
 *
 * flink 状态和容错 容错恢复就是从状态、checkpoint进行恢复
 * https://s0.lgstatic.com/i/image/M00/09/8E/Ciqc1F68r6OAMS0kAADjsuTilgw677.png
 *
 * 在 Flink 中，根据数据集是否按照某一个 Key 进行分区，将状态分为 Keyed State 和 Operator State（Non-Keyed State）两种类型。
 *
 * Keyed State 是经过分区后的流上状态，每个 Key 都有自己的状态。
 * 与 Keyed State 不同的是，Operator State 可以用在所有算子上，每个算子子任务或者说每个算子实例共享一个状态，流入这个算子子任务的数据可以访问和更新这个状态。每个算子子任务上的数据共享自己的状态。
 *
 * 无论是 Keyed State 还是 Operator State，Flink 的状态都是基于本地的，即每个算子子任务维护着这个算子子任务对应的状态存储，算子子任务之间的状态不能相互访问。
 *
 * Keyed State，Flink 提供了几种现成的数据结构供我们使用，State 主要有四种实现，分别为 ValueState、MapState、AppendingState 和 ReadOnlyBrodcastState ，其中 AppendingState 又可以细分为 ReducingState、AggregatingState 和 ListState。
 *
 * Operator State 的实际应用场景不如 Keyed State 多，一般来说它会被用在 Source 或 Sink 等算子上，用来保存流入数据的偏移量或对输出数据做缓存，以保证 Flink 应用的 Exactly-Once 语义。
 *
 *
 * @author tomgs
 * @since 2021/4/20
 */
public class FlinkStateDemo {

  final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

  DataStreamSource<Tuple2<Long, Long>> streamSource;

  @Before
  public void before() {
    streamSource = env.fromElements(
        Tuple2.of(1L, 3L),
        Tuple2.of(1L, 5L),
        Tuple2.of(1L, 7L),
        Tuple2.of(1L, 5L),
        Tuple2.of(1L, 2L));
  }

  @After
  public void after() throws Exception {
    env.execute("submit job");
  }

  @Test
  public void valueSate() {
    streamSource.keyBy(0)
        .flatMap(new CountWindowAverage())
        .printToErr();
  }

  @Test
  public void listSate() {
    streamSource.keyBy(0)
        .flatMap(new RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>() {

          private transient ListState<Tuple2<Long, Long>> sumList;

          @Override
          public void open(Configuration parameters) throws Exception {
            ListStateDescriptor<Tuple2<Long, Long>> descriptor = new ListStateDescriptor<>(
                "list", TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() { }));
            sumList = getRuntimeContext().getListState(descriptor);
          }

          @Override
          public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long, Long>> out) throws Exception {
            if (value.f1 % 2 != 0) {
              sumList.add(value);
            } else { // 遇到偶数进行输出
              for (Tuple2<Long, Long> tuple2 : sumList.get()) {
                out.collect(tuple2);
              }
              sumList.clear();
            }

          }
        })
        .printToErr();
  }

  @Test
  public void mapState() {
    streamSource.keyBy(0)
        .flatMap(new RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>() {

          private transient MapState<Long, Tuple2<Long, Long>> mapState;

          @Override
          public void open(Configuration parameters) throws Exception {
            MapStateDescriptor<Long, Tuple2<Long, Long>> mapStateDescriptor =
                new MapStateDescriptor<>("map", TypeInformation.of(Long.class), TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {}));
            mapState = getRuntimeContext().getMapState(mapStateDescriptor);
          }

          @Override
          public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long, Long>> out) throws Exception {
            if (mapState.contains(value.f0)) {
              Tuple2<Long, Long> longLongTuple = mapState.get(value.f0);
              mapState.put(value.f0, new Tuple2<>(value.f0, longLongTuple.f1 + value.f1));
            } else {
              mapState.put(value.f0, value);
            }

            out.collect(mapState.get(value.f0));
          }

        })
        .printToErr();
  }

  @Test
  public void aggregatingSate() {
    streamSource.keyBy(0)
        .flatMap(new RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>() {
          private transient AggregatingState<Tuple2<Long, Long>, Tuple2<Long, Long>> aggregatingState;
          @Override
          public void open(Configuration parameters) throws Exception {
            AggregatingStateDescriptor<Tuple2<Long, Long>, Tuple2<Long, Long>, Tuple2<Long, Long>> stateDescriptor =
                new AggregatingStateDescriptor<>("sum",
                    new SumAggregateFunction(),
                    TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {}));
            aggregatingState = getRuntimeContext().getAggregatingState(stateDescriptor);
          }

          @Override
          public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long, Long>> out) throws Exception {
            aggregatingState.add(value);
            out.collect(Tuple2.of(value.f0, aggregatingState.get().f1));
          }

        })
        .printToErr();
  }

  @Test
  public void reducingSate() {
//    ReducingState<Tuple2<Long, Long>> reducingState;
//    reducingState.add(value);
//    reducingState.get();
  }

  @Test
  public void broadcastSate() {
//    BroadcastState<Long, Long> broadcastState;
//    broadcastState.put(key, value);
//    broadcastState.get(key);
//    broadcastState.remove(key);
  }

  public static class CountWindowAverage extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>> {

    private transient ValueState<Tuple2<Long, Long>> sum;

    public void flatMap(Tuple2<Long, Long> input, Collector<Tuple2<Long, Long>> out) throws Exception {
      Tuple2<Long, Long> currentSum;
      // 访问ValueState
      if (sum.value() == null) {
        currentSum = Tuple2.of(0L, 0L);
      } else {
        currentSum = sum.value();
      }
      // 更新
      currentSum.f0 += 1;
      // 第二个元素加1
      currentSum.f1 += input.f1;
      // 更新state
      sum.update(currentSum);
      // 如果count的值大于等于2，求知道并清空state
      if (currentSum.f0 >= 2) {
        out.collect(new Tuple2<>(input.f0, currentSum.f1 / currentSum.f0));
        sum.clear();
      }
    }

    public void open(Configuration config) {
      ValueStateDescriptor<Tuple2<Long, Long>> descriptor =
          new ValueStateDescriptor<>(
              "average", // state的名字
              TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {
              })
          ); // 设置默认值

      // 设置状态过期时间
      StateTtlConfig ttlConfig = StateTtlConfig
          .newBuilder(Time.seconds(10))
          .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
          .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
          .build();

      descriptor.enableTimeToLive(ttlConfig);
      sum = getRuntimeContext().getState(descriptor);
    }

  }


  private static class SumAggregateFunction implements AggregateFunction<Tuple2<Long, Long>, Tuple2<Long, Long>, Tuple2<Long, Long>> {

    @Override
    public Tuple2<Long, Long> createAccumulator() {
      return new Tuple2<>(0L, 0L);
    }

    @Override
    public Tuple2<Long, Long> add(Tuple2<Long, Long> value, Tuple2<Long, Long> accumulator) {
      return new Tuple2<>(value.f0, value.f1 + accumulator.f1);
    }

    @Override
    public Tuple2<Long, Long> getResult(Tuple2<Long, Long> accumulator) {
      return accumulator;
    }

    @Override
    public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
      return null;
    }

  }
}
