package com.tomgs.scheduler.quartz;

import cn.hutool.core.util.ReflectUtil;
import com.tomgs.scheduler.quartz.job.InitJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author tangzy
 * @since 1.0
 */
public class Demo1 {

  public static void main(String[] args) throws SchedulerException, InterruptedException {
    //定时器对象
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
    scheduler.standby();

    QuartzScheduler sched = (QuartzScheduler) ReflectUtil.getFieldValue(scheduler, "sched");
    QuartzSchedulerResources resources = (QuartzSchedulerResources) ReflectUtil.getFieldValue(sched, "resources");
    int numberOfJobs = resources.getJobStore().getNumberOfJobs();

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
    //scheduler.scheduleJob(job, trigger);
    scheduler.start();

    int numberOfJobs1 = resources.getJobStore().getNumberOfJobs();

    Thread.sleep(1000 * 10);
    System.out.println("切换为standby状态...");
    scheduler.standby();

    System.out.println(scheduler.isStarted());
    System.out.println(scheduler.isInStandbyMode());

    // and start it off
    //开始定时任务
    Thread.sleep(1000 * 10);
    System.out.println("切换为active状态...");
    scheduler.start();
    scheduler.clear();
    scheduler.scheduleJob(job, trigger);
    System.out.println(scheduler.isInStandbyMode());
  }

}
