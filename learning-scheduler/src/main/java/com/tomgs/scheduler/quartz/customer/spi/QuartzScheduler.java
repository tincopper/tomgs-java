package com.tomgs.scheduler.quartz.customer.spi;

import com.tomgs.scheduler.quartz.customer.BasicScheduler;
import com.tomgs.scheduler.quartz.customer.JobInfo;
import com.tomgs.scheduler.quartz.job.InitJob;
import java.util.List;
import java.util.Properties;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author tangzy
 * @since 1.0
 */
public class QuartzScheduler implements BasicScheduler {

  private Scheduler scheduler;

  public QuartzScheduler() throws SchedulerException {
    //this(StdSchedulerFactory.getDefaultScheduler());
    StdSchedulerFactory factory = new StdSchedulerFactory();
    factory.initialize(getBaseQuartzProperties());
    this.scheduler = factory.getScheduler();
  }

  public QuartzScheduler(final Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  private Properties getBaseQuartzProperties() {
    Properties result = new Properties();
    result.put("org.quartz.threadPool.class", org.quartz.simpl.SimpleThreadPool.class.getName());
    result.put("org.quartz.threadPool.threadCount", "1");
    result.put("org.quartz.scheduler.instanceName", "tomgs-scheduler");
    result.put("org.quartz.jobStore.misfireThreshold", "1");
    //result.put("org.quartz.plugin.shutdownhook.class", Class.class);
    //result.put("org.quartz.plugin.shutdownhook.cleanShutdown", Boolean.TRUE.toString());
    return result;
  }

  @Override
  public void config() throws Exception {

  }

  @Override
  public void start() throws Exception {
    scheduler.start();
  }

  @Override
  public void startDelayed(int seconds) throws Exception {
    scheduler.startDelayed(seconds);
  }

  @Override
  public void stop() throws Exception {
    scheduler.shutdown();
  }

  @Override
  public void addJob(JobInfo jobInfo) throws Exception {
    Class<? extends Job> jobClass;
    if (jobInfo.getType() == 0) {
      jobClass = InitJob.class;
    } else {
      jobClass = InitJob.class;
    }
    JobDetail jobDetail = JobBuilder.newJob(jobClass)
        .withIdentity(jobInfo.getJobName(), jobInfo.getGroupName())
        .setJobData(new JobDataMap(jobInfo.getJobData()))
        .build();
    /*Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobInfo.getJobName(), jobInfo.getGroupName())
        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3))
        .withPriority(jobInfo.getPriority().getValue())
        .build();*/

    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobInfo.getJobName(), jobInfo.getGroupName())
        .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCron()))
        .withPriority(jobInfo.getPriority().getValue())
        .build();

    //scheduler.addJob(jobDetail, true);
    scheduler.scheduleJob(jobDetail, trigger);
  }

  @Override
  public boolean deleteJob(JobInfo jobInfo) throws Exception {
    return scheduler.deleteJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
  }

  @Override
  public boolean deleteJobs(List<JobInfo> jobInfoList) throws Exception {
    boolean result = true;
    for (JobInfo jobInfo : jobInfoList) {
      result = deleteJob(jobInfo);
    }
    return result;
  }

  @Override
  public boolean checkExists(JobInfo jobInfo) throws Exception {
    return scheduler.checkExists(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
  }

  @Override
  public void pauseJob(JobInfo jobInfo) throws Exception {
    scheduler.pauseJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
  }

  @Override
  public boolean isPaused(JobInfo jobInfo) throws Exception {
    return scheduler.isShutdown() && Trigger.TriggerState.PAUSED == scheduler.getTriggerState(new TriggerKey(jobInfo.getJobName(), jobInfo.getGroupName()));
  }

  @Override
  public void clear() throws Exception {
    scheduler.clear();
  }

  @Override
  public void resumeJob(JobInfo jobInfo) throws Exception {
    scheduler.resumeJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
  }

  @Override
  public void triggerJob(JobInfo jobInfo) throws Exception {
    scheduler.triggerJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
  }

  @Override
  public int getCurrentlyExecutingJobs() throws Exception {
    return scheduler.getCurrentlyExecutingJobs().size();
  }

  @Override
  public boolean isShutdown() throws Exception {
    return scheduler.isShutdown();
  }

  @Override
  public boolean isStarted() throws Exception {
    return scheduler.isStarted();
  }

}
