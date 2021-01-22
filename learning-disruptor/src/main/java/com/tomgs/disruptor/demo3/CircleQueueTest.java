package com.tomgs.disruptor.demo3;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/1/21
 */
public class CircleQueueTest {

  @Test
  public void testBase() {
    CircleQueue<String> queue = new CircleQueue<>(4, new AbortEnqueuePolicy<>());
    if (queue.isEmpty()) {
      System.out.println("队列为空");
    }
    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
    System.out.println("添加元素...");
    queue.offer("a");
    if (!queue.isEmpty()) {
      System.out.println("队列不为空");
    }
    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
    queue.offer("b");
    queue.offer("c");
    queue.offer("d");
    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
    System.out.println(queue.toString());

    if (queue.isFull()) {
      System.out.println("队列已满");
      System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
      String poll = queue.poll();
      System.out.println("出队元素：" + poll);
    }

    queue.offer("e");
    System.out.println(queue.toString());
    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());

    // 设置入队拒绝策略
    queue.setRejectedEnqueueHandler(new DiscardOldestElementPolicy<>());
    queue.offer("f");

    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
    System.out.println(queue.toString());

    // 设置入队拒绝策略
    queue.setRejectedEnqueueHandler(new ResizeQueuePolicy<>());
    queue.offer("g");
    queue.offer("h");
    queue.offer("j");
    queue.offer("j");
    queue.offer("j");
    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
    System.out.println(queue.toString());

    while (!queue.isEmpty()) {
      System.out.println(queue.poll());
    }

    queue.offer("k");
    System.out.println("队列大小：" + queue.size() + ", 容量：" + queue.getCapacity());
    System.out.println(queue.toString());
  }

  @Test
  public void testShrinksCapacity() {
    CircleQueue<String> queue = new CircleQueue<>(16, new AbortEnqueuePolicy<>());
    queue.offer("a");
    queue.offer("b");
    queue.offer("c");
    queue.offer("d");
    queue.offer("e");

    System.out.println(queue.poll());

    System.out.println(queue);

    Assert.assertEquals(queue.getCapacity(), 10);
  }

}
