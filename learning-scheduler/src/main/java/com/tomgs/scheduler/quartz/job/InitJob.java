package com.tomgs.scheduler.quartz.job;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author tangzy
 * @since 1.0
 */
public class InitJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    System.out.println(Thread.currentThread().getName() + " ..........init job");
    String fireInstanceId = context.getFireInstanceId();
    JobDataMap map = context.getMergedJobDataMap();
    String key = map.getString("key");
    System.out.println("fireInstanceId: " + fireInstanceId + ", key: " + key);

    Date nextFireTime = context.getNextFireTime();
    Date previousFireTime = context.getPreviousFireTime();
    System.out.println("上一次执行时间：" + previousFireTime + "下一次执行时间：" + nextFireTime);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}