package com.tomgs.flink.demo.log;

import com.tomgs.flink.demo.log.model.AggResult;
import com.tomgs.flink.demo.log.model.RuleMatchedLogEvent;
import com.tomgs.flink.demo.log.model.SummaryStatistics;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 聚合计算
 *
 * @author tomgs
 * @since 2021/4/22
 */
public class AggWindowFunction extends KeyedProcessFunction<String, RuleMatchedLogEvent, AggResult> {

  // ruleId -> AggResult
  private transient ValueState<AggResult> aggResultState;

  private transient ValueState<Long> timerState;

  private static final long DURATION = 3 * 1000;

  @Override
  public void open(Configuration parameters) {
    ValueStateDescriptor<AggResult> aggStateDescriptor = new ValueStateDescriptor<>("ruleAgg", AggResult.class);
    this.aggResultState = getRuntimeContext().getState(aggStateDescriptor);

    ValueStateDescriptor<Long> timerDescriptor = new ValueStateDescriptor<>("timer-state", Types.LONG);
    this.timerState = getRuntimeContext().getState(timerDescriptor);
  }

  @Override
  public void processElement(RuleMatchedLogEvent event, Context context, Collector<AggResult> out) throws Exception {
    AggResult result = aggResultState.value();
    if (result != null) {
      // 计算操作
      result.getStatistics().accept(event.getLogEvent().getMessage().getInt("cpu", 20));
      aggResultState.update(result);
      return;
    }

    AggResult aggResult = AggResult.builder()
        .ruleId(event.getRuleId())
        .alarmRule(event.getAlarmRule())
        .logEvent(event.getLogEvent())
        .matchedResult(event.getMatchedResult())
        .statistics(new SummaryStatistics())
        .build();

    aggResult.getStatistics().accept(event.getLogEvent().getMessage().getInt("cpu", 20));
    aggResultState.update(aggResult);

    // 添加定时器
    long timer = context.timerService().currentProcessingTime() + DURATION;
    context.timerService().registerProcessingTimeTimer(timer);
    timerState.update(timer);
  }

  @Override
  public void onTimer(long timestamp, OnTimerContext ctx, Collector<AggResult> out) throws Exception {
    // 窗口时间到了，发送数据
    out.collect(aggResultState.value());

    ctx.timerService().deleteProcessingTimeTimer(timerState.value());
    timerState.clear();
    aggResultState.clear();
  }

}
