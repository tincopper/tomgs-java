package com.tomgs.disruptor.demo3.executor;

/**
 * job runnable
 *
 * @author tomgs
 * @since 2021/1/21
 */
public abstract class JobRunnable {

  public void run() {
    try {
      execute();
    } catch (Exception e) {
      exceptionCaught(e);
    }

  }

  public abstract void execute() throws Exception;

  public void exceptionCaught(Exception e) {
    //默认不处理,交由子类处理处理任务出现异常时的逻辑
  }

}
