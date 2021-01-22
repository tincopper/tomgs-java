package com.tomgs.disruptor.demo3;

import java.util.Queue;

/**
 * 入队拒绝策略
 *
 * @author tomgs
 * @since 2021/1/21
 */
public interface RejectedEnqueueHandler<T> {

  void rejectedExecution(T e, Queue<T> queue);

}
