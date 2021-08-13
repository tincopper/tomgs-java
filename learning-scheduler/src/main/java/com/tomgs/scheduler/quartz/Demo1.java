package com.tomgs.scheduler.quartz;

import com.tomgs.scheduler.quartz.job.InitJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author tangzy
 * @since 1.0
 */
public class Demo1 {

  public static void main(String[] args) throws SchedulerException {
    //定时器对象
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
    scheduler.standby();
    //定义一个工作对象 设置工作名称与组名
    JobDataMap dataMap = new JobDataMap();
    dataMap.put("key", "123");

    JobDetail job = JobBuilder.newJob(InitJob.class).withIdentity("job1", "group1").setJobData(dataMap).build();
    //定义一个触发器 简单Trigger 设置工作名称与组名 5秒触发一次
/*    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)).build();*/
    //定义一个任务调度的Trigger 设置工作名称与组名 每天的24:00触发一次
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").withSchedule(
        CronScheduleBuilder.cronSchedule("0/3 * * * * ?")).build();
    //设置工作 与触发器
    scheduler.scheduleJob(job, trigger);
    scheduler.scheduleJob(job, trigger);
    //scheduler.start();
    //scheduler.standby();
    // and start it off
    //开始定时任务
    //scheduler.start();
  }

}
