package com.tomgs.disruptor.demo3.executor;

import com.tomgs.disruptor.demo3.CircleQueue;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务executor</br>
 * PS: 但是任务为顺序执行
 *
 * @author tomgs
 * @since 2021/1/22
 */
public class JobExecutor {

  private final ReentrantLock lock = new ReentrantLock();

  private AtomicBoolean isStarted = new AtomicBoolean(false);

  private CircleQueue<JobRunnable> circleQueue;

  private Executor executor;

  public JobExecutor() {

  }

  public JobExecutor(final CircleQueue<JobRunnable> circleQueue, final Executor executor) {
    this.circleQueue = circleQueue;
    this.executor = executor;
  }

  public CircleQueue<JobRunnable> getCircleQueue() {
    return circleQueue;
  }

  public void setCircleQueue(
      CircleQueue<JobRunnable> circleQueue) {
    this.circleQueue = circleQueue;
  }

  public Executor getExecutor() {
    return executor;
  }

  public void setExecutor(Executor executor) {
    this.executor = executor;
  }

  public void submit(JobRunnable runnable) {
    lock.lock();
    try {
      boolean offered = circleQueue.offer(runnable);
      if (!offered) {
        System.out.println("queue offer false. 请检查任务入队策略...");
        return;
      }
    } finally {
      lock.unlock();
    }

    // 从第一个任务触发，后续自动执行
    if (isStarted.compareAndSet(false, true) && circleQueue.size() >= 1) {
      notifyNextJobExecute();
    }

  }

  private void restart() {
    isStarted.compareAndSet(true, false);
  }

  /**
   * 通知下一个任务开始执行
   */
  private void notifyNextJobExecute() {
    if (circleQueue.isEmpty()) {
      // 对于空队列再添加元素时需要重新开始消费，否则将会导致队列任务不消费
      restart();
      return;
    }

    JobRunnable runnable = Objects.requireNonNull(circleQueue.poll());
    executor.execute(() -> {
      try {
        runnable.run();
      } catch (Exception e) {
        runnable.exceptionCaught(e);
      } finally {
        notifyNextJobExecute();
      }
    });
  }

}
