package com.tomgs.disruptor.demo3;

import java.util.Queue;

/**
 * @author tomgs
 * @since 2021/1/21
 */
public class DiscardOldestElementPolicy<T> implements RejectedEnqueueHandler<T> {

  @Override
  public void rejectedExecution(T e, Queue<T> queue) {
    Object old = queue.poll();
    System.out.println("抛弃旧的元素：" + old);
    //queue.offer(e);
  }

}
