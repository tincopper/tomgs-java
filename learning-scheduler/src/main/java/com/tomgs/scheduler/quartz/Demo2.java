package com.tomgs.scheduler.quartz;

import com.tomgs.scheduler.quartz.job.DelayJob;
import com.tomgs.scheduler.quartz.listener.TimeDelayedListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.spi.MutableTrigger;

import java.util.Date;

/**
 * @author tangzy
 * @since 1.0
 */
public class Demo2 {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        //定时器对象
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.standby();

        //定义一个工作对象 设置工作名称与组名
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("key", "123");

        JobKey jobKey = new JobKey("job1", "group1");
        JobDetail job = JobBuilder.newJob(DelayJob.class)
                .withIdentity(jobKey)
                .setJobData(dataMap)
                .build();
        //定义一个触发器 简单Trigger 设置工作名称与组名 5秒触发一次
        final TriggerKey triggerKey = new TriggerKey("trigger1", "group1");
        /*Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)
                        .withMisfireHandlingInstructionIgnoreMisfires())
                .build();*/
        MutableTrigger newTrigger = SimpleScheduleBuilder.simpleSchedule().build();
        Date nextFireTime = new Date(System.currentTimeMillis() + 5000); // 当前时间加上10秒的延迟
        newTrigger.setJobKey(jobKey);
        newTrigger.setKey(triggerKey);
        newTrigger.setStartTime(nextFireTime);
        //设置工作 与触发器
        scheduler.scheduleJob(job, newTrigger);
        //scheduler.scheduleJob(job, trigger);
        scheduler.start();
        scheduler.getListenerManager().addJobListener(new TimeDelayedListener(), KeyMatcher.keyEquals(jobKey));
        Thread stateChecker = new Thread(() -> {
            try {
                for (; ; ) {
                    final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    //System.out.println(jobDetail.getJobDataMap().getWrappedMap());
                    Thread.sleep(1000);
                }
            } catch (SchedulerException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        stateChecker.start();
    }

}
