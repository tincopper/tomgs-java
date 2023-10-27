package com.tomgs.scheduler.quartz.job;

import cn.hutool.core.date.DateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author tangzy
 * @since 1.0
 */
public class DelayJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    final Date sdate = DateUtil.date();
    //System.out.println("开始时间：" + sdate);
    try {
      Thread.sleep(7000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    final Date enddate = DateUtil.date();
    System.out.println("开始时间：" + sdate + "结束时间：" + enddate);
  }
}