package com.tomgs.guice.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tangzy
 * @since 1.0
 */
@SuppressWarnings("UnstableApiUsage")
public class EventBusDemo {

  private EventBus eventBus = new EventBus();

  private void post(Object event) {
    eventBus.post(event);
  }

  private void register(Object listener) {
    eventBus.register(listener);
  }

  // 定义事件
  private static class EventA {
    @Override
    public String toString() {
      return "EventA{}";
    }
  }

  private static class EventB {
    @Override
    public String toString() {
      return "EventB{}";
    }
  }

  // 事件监听
  private static class EventListener {
    // 这个注解也可以作用在接口定义的方法上
    @Subscribe
    public void onEvent(EventA e) {
      System.out.println(Thread.currentThread().getName() + "我订阅的是 A事件,接收到:" + e);
    }

    @Subscribe
    public void onEvent(EventB e) {
      System.out.println(Thread.currentThread().getName() + "我订阅的是 B事件,接收到:" + e);
    }

    @Subscribe
    public void onEvent(DeadEvent de) {
      System.out.println(Thread.currentThread().getName() + "发布了错误的事件:" + de.getEvent());
    }

  }

  public static void main(String[] args) {
    // 同步
    EventBusDemo eventBusDemo = new EventBusDemo();
    eventBusDemo.register(new EventListener());
    EventA eventA = new EventA();
    eventBusDemo.post(eventA);
    EventB eventB = new EventB();
    eventBusDemo.post(eventB);
    eventBusDemo.post("12312");

    // 异步
    ExecutorService threadPool = Executors.newCachedThreadPool();
    EventBus eb = new AsyncEventBus(threadPool);
    eb.register(new EventListener());
    eb.post(new EventA());
    eb.post(new EventB());

    //未订阅事件
    eb.post("456789");
    threadPool.shutdown();
  }

}
