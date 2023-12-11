package com.tomgs.scheduler.quartz.listener;

import org.quartz.*;
import org.quartz.spi.MutableTrigger;

import java.util.Date;

/**
 * TimeDelayedListener
 *
 * @author tomgs
 * @since 1.0
 */
public class TimeDelayedListener implements JobListener {

    @Override
    public String getName() {
        return "TimeDelayedListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("Job监听器：MyJobListener.jobToBeExecuted()");
        final JobDetail jobDetail = context.getJobDetail();
        final JobKey key = jobDetail.getKey();
        try {
            // 任务暂停
            context.getScheduler().pauseJob(key);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println("Job监听器：MyJobListener.jobExecutionVetoed()");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("Job监听器：MyJobListener.jobWasExecuted()");
        final JobDetail jobDetail = context.getJobDetail();
        final Trigger trigger = context.getTrigger();
        MutableTrigger newTrigger = SimpleScheduleBuilder.simpleSchedule().build();
        Date nextFireTime = new Date(System.currentTimeMillis() + 5000); // 当前时间加上10秒的延迟
        newTrigger.setJobKey(jobDetail.getKey());
        newTrigger.setKey(trigger.getKey());
        newTrigger.setStartTime(nextFireTime);
        newTrigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        try {

            if (context.getScheduler().checkExists(jobDetail.getKey())) {
                JobDetail jobDetail1 = context.getScheduler().getJobDetail(jobDetail.getKey());
                System.out.println(jobDetail1);
            }

            context.getScheduler().rescheduleJob(trigger.getKey(), newTrigger);
            // context.getScheduler().scheduleJob(jobDetail, Sets.newHashSet(newTrigger), true);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
