package com.tomgs.disruptor.demo2.impl;

import com.tomgs.disruptor.demo2.QueueConsumerExecutor;

/**
 * @author tomgs
 * @since 2020/9/22
 */
public class TestConsumerExecutor extends QueueConsumerExecutor<String> {

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + "--------------" + getData());
  }

}
