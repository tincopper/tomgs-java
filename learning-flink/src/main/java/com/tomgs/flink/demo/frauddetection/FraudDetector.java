package com.tomgs.flink.demo.frauddetection;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

/**
 * 骗子们在小额交易后不会等很久就进行大额消费，这样可以降低小额测试交易被发现的几率。 比如，假设你为欺诈检测器设置了一分钟的超时，对于上边的例子，交易 3 和 交易 4 只有间隔在一分钟之内才被认为是欺诈交易。 Flink 中的 KeyedProcessFunction 允许您设置计时器，该计时器在将来的某个时间点执行回调函数。
 *
 * 让我们看看如何修改程序以符合我们的新要求：
 *
 * 当标记状态被设置为 true 时，设置一个在当前时间一分钟后触发的定时器。
 * 当定时器被触发时，重置标记状态。
 * 当标记状态被重置时，删除定时器。
 */
public class FraudDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

  private static final long serialVersionUID = 1L;

  private static final double SMALL_AMOUNT = 1.00;
  private static final double LARGE_AMOUNT = 500.00;
  private static final long ONE_MINUTE = 60 * 1000;

  private transient ValueState<Boolean> flagState;

  private transient ValueState<Long> timerState;

  @Override
  public void open(Configuration parameters) {
    // 状态在使用之前需要先被注册。 状态需要使用 open() 函数来注册状态。
    ValueStateDescriptor<Boolean> flagDescriptor = new ValueStateDescriptor<>("flag", Types.BOOLEAN);
    flagState = getRuntimeContext().getState(flagDescriptor);

    ValueStateDescriptor<Long> timerDescriptor = new ValueStateDescriptor<>("timer-state", Types.LONG);
    timerState = getRuntimeContext().getState(timerDescriptor);
  }
  @Override
  public void processElement(
      Transaction transaction,
      Context context,
      Collector<Alert> collector) throws Exception {

    // Get the current state for the current key
    Boolean lastTransactionWasSmall = flagState.value();

    // Check if the flag is set
    if (lastTransactionWasSmall != null) {
      if (transaction.getAmount() > LARGE_AMOUNT) {
        //Output an alert downstream
        Alert alert = new Alert();
        alert.setId(transaction.getAccountId());

        collector.collect(alert);
      }
      // Clean up our state
      cleanUp(context);
    }

    if (transaction.getAmount() < SMALL_AMOUNT) {
      // set the flag to true
      flagState.update(true);

      long timer = context.timerService().currentProcessingTime() + ONE_MINUTE;
      context.timerService().registerProcessingTimeTimer(timer);

      timerState.update(timer);
    }
  }

  @Override
  public void onTimer(long timestamp, OnTimerContext ctx, Collector<Alert> out) {
    // remove flag after 1 minute
    timerState.clear();
    flagState.clear();
  }

  private void cleanUp(Context ctx) throws Exception {
    // delete timer
    Long timer = timerState.value();
    ctx.timerService().deleteProcessingTimeTimer(timer);

    // clean up all state
    timerState.clear();
    flagState.clear();
  }

}
