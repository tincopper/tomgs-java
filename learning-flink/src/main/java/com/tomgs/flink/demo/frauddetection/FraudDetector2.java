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
 * 对于一个账户，如果出现小于 $1 美元的交易后紧跟着一个大于 $500 的交易，就输出一个报警信息。
 */
public class FraudDetector2 extends KeyedProcessFunction<Long, Transaction, Alert> {

  private static final long serialVersionUID = 1L;

  private static final double SMALL_AMOUNT = 1.00;
  private static final double LARGE_AMOUNT = 500.00;
  private static final long ONE_MINUTE = 60 * 1000;

  private transient ValueState<Boolean> flagState;

  @Override
  public void open(Configuration parameters) throws Exception {
    // 状态在使用之前需要先被注册。 状态需要使用 open() 函数来注册状态。
    ValueStateDescriptor<Boolean> flagDescriptor = new ValueStateDescriptor<>("flag", Types.BOOLEAN);
    flagState = getRuntimeContext().getState(flagDescriptor);
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
        // Output an alert downstream
        Alert alert = new Alert();
        alert.setId(transaction.getAccountId());

        collector.collect(alert);
      }

      // Clean up our state
      flagState.clear();
    }

    if (transaction.getAmount() < SMALL_AMOUNT) {
      // Set the flag to true
      flagState.update(true);
    }
  }

}
