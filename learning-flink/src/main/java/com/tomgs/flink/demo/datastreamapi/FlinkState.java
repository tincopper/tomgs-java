package com.tomgs.flink.demo.datastreamapi;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * flink 状态和容错 容错恢复就是从状态、checkpoint进行恢复
 *
 * @author tomgs
 * @since 2021/4/20
 */
public class FlinkState {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.fromElements(Tuple2.of(1L, 3L), Tuple2.of(1L, 5L), Tuple2.of(1L, 7L), Tuple2.of(1L, 5L),
        Tuple2.of(1L, 2L))
        .keyBy(0)
        .flatMap(new CountWindowAverage())
        .printToErr();
    env.execute("submit job");
  }


  public static class CountWindowAverage extends
      RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>> {

    private transient ValueState<Tuple2<Long, Long>> sum;

    public void flatMap(Tuple2<Long, Long> input, Collector<Tuple2<Long, Long>> out)
        throws Exception {
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

      StateTtlConfig ttlConfig = StateTtlConfig
          .newBuilder(Time.seconds(10))
          .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
          .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
          .build();

      descriptor.enableTimeToLive(ttlConfig);
      sum = getRuntimeContext().getState(descriptor);

    }

  }


}
