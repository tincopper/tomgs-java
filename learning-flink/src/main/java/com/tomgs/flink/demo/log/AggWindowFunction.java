package com.tomgs.flink.demo.log;

import cn.hutool.json.JSONObject;
import com.tomgs.flink.demo.log.model.AggResult;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 聚合计算
 *
 * @author tomgs
 * @since 2021/4/22
 */
public class AggWindowFunction extends KeyedProcessFunction<String, Tuple2<String, JSONObject>, String> {

  private transient MapState<String, AggResult> aggResultMapState;

  @Override
  public void open(Configuration parameters) throws Exception {
    MapStateDescriptor<String, AggResult> aggStateDescriptor = new MapStateDescriptor<>("agg",
        String.class, AggResult.class);
    this.aggResultMapState = getRuntimeContext().getMapState(aggStateDescriptor);
  }

  @Override
  public void processElement(Tuple2<String, JSONObject> value, Context ctx, Collector<String> out) throws Exception {
      out.collect(value.f1.toString());
  }

  @Override
  public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
    super.onTimer(timestamp, ctx, out);
  }

}
