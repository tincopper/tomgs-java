package com.tomgs.scheduler.dis;

import com.tomgs.scheduler.quartz.customer.BasicScheduler;
import com.tomgs.scheduler.quartz.customer.NewJobRequest;
import com.tomgs.scheduler.quartz.customer.SchedulerManager;
import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import java.io.IOException;
import org.junit.Test;

/**
 * @author tangzy
 * @since 1.0
 */
public class JobLoadTest {

  private static final int LIMIT = 10000;

  @Test
  public void testJobLoad() throws IOException {
    long start = System.currentTimeMillis();
    SchedulerConfig config = SchedulerConfig.builder().misfireThreshold(5000).schedulerName("scheduler").threadCount(8).build();
    BasicScheduler scheduler = SchedulerManager.INSTANCE.createScheduler(config);
    scheduler.start();
    for (int i = 0; i < LIMIT; i++) {

      //scheduler.config(config);
      //scheduler1.config(config1);

      NewJobRequest job = new NewJobRequest();
      job.setGroupName("groupName");
      job.setJobName("jobName" + i);
      job.setCron("*/3 * * * * ?");

      scheduler.addJob(job);
    }
    long end = System.currentTimeMillis();

    System.out.println("result: " + (end - start));
    System.in.read();
  }
}
