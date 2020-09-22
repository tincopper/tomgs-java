package com.tomgs.disruptor.demo2.impl;

import com.tomgs.disruptor.demo2.QueueConsumerExecutor;
import com.tomgs.disruptor.demo2.QueueConsumerFactory;

/**
 * @author tomgs
 * @since 2020/9/22
 */
public class TestConsumerFactory implements QueueConsumerFactory<String> {

  @Override
  public QueueConsumerExecutor<String> create() {
    return new TestConsumerExecutor();
  }

  @Override
  public String fixName() {
    return "test";
  }

}
