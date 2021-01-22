package com.tomgs.disruptor.demo3;

import java.util.Queue;

/**
 * @author tomgs
 * @since 2021/1/21
 */
public class AbortEnqueuePolicy<T> implements RejectedEnqueueHandler<T> {

  @Override
  public void rejectedExecution(T e, Queue<T> queue) {
    throw new RuntimeException(String.format("队列已满 [%s]，将终止添加元素: [%s]", queue, e));
  }

}
