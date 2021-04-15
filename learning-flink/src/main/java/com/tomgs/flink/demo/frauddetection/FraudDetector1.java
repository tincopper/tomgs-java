package com.tomgs.flink.demo.frauddetection;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

public class FraudDetector1 extends KeyedProcessFunction<Long, Transaction, Alert> {

  private static final long serialVersionUID = 1L;

  private static final double SMALL_AMOUNT = 1.00;
  private static final double LARGE_AMOUNT = 500.00;
  private static final long ONE_MINUTE = 60 * 1000;

  @Override
  public void processElement(
      Transaction transaction,
      Context context,
      Collector<Alert> collector) throws Exception {

    Alert alert = new Alert();
    alert.setId(transaction.getAccountId());

    collector.collect(alert);
  }

}
