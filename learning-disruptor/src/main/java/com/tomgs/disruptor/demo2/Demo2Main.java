package com.tomgs.disruptor.demo2;

import com.tomgs.disruptor.demo2.impl.TestConsumerFactory;

/**
 * @author tomgs
 * @since 2020/9/22
 */
public class Demo2Main {

  public static void main(String[] args) throws InterruptedException {
    QueueProviderManager<String> manager = new QueueProviderManager<>(new TestConsumerFactory());
    manager.start();
    QueueProvider<String> provider = manager.getProvider();
    for (long l = 0; true; l++) {
      // 生产数据
      long finalL = l;
      provider.onData(stringQueueEvent -> stringQueueEvent.setData("test" + finalL));
      Thread.sleep(1000);
    }
  }

}
