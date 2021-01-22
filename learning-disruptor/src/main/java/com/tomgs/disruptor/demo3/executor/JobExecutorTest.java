package com.tomgs.disruptor.demo3.executor;

import com.tomgs.disruptor.demo3.CircleQueue;
import com.tomgs.disruptor.demo3.ResizeQueuePolicy;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/1/22
 */
public class JobExecutorTest {

  @Test
  public void testJobExecutor() throws IOException {

    ExecutorService demoExecutor = Executors.newFixedThreadPool(10);

    CircleQueue<JobRunnable> queue = new CircleQueue<>(16, new ResizeQueuePolicy<>());
    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
    JobExecutor executor = new JobExecutor();
    executor.setCircleQueue(queue);
    executor.setExecutor(threadPoolExecutor);

    for (int i = 0; i < 100; i++) {
      int finalI = i;
      executor.submit(new JobRunnable() {
        @Override
        public void execute() throws Exception {
          System.out.println("" + finalI);
          //Thread.sleep(new Random().nextInt(2000));
          //System.out.println("---------");
        }
      });
    }

    System.in.read();
  }

}
