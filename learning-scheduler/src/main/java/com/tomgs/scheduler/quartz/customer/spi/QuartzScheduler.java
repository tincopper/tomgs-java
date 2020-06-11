package com.tomgs.scheduler.quartz.customer.spi;

import com.tomgs.scheduler.quartz.customer.BasicScheduler;
import com.tomgs.scheduler.quartz.customer.JobInfo;
import com.tomgs.scheduler.quartz.customer.JobTypeManager;
import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import com.tomgs.scheduler.quartz.customer.exeception.TomgsSchedulerException;
import com.tomgs.scheduler.quartz.customer.extension.Join;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
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
@Join
public class QuartzScheduler implements BasicScheduler {

  private String schedulerName;
  private Scheduler scheduler;
  private StdSchedulerFactory factory;
  private ReentrantLock lock;

  public QuartzScheduler() {
    factory = new StdSchedulerFactory();
    lock = new ReentrantLock();
  }

  public QuartzScheduler(SchedulerConfig config) {
    this();
    config(config);
  }

  @Override
  public void config(SchedulerConfig config) {
    try {
      Properties result = getBaseProperties(config);
      this.factory.initialize(result);
      this.scheduler = factory.getScheduler();
      this.schedulerName = config.getSchedulerName();
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  private Properties getBaseProperties(SchedulerConfig config) {
    Properties result = new Properties();
    result.put("org.quartz.threadPool.class", org.quartz.simpl.SimpleThreadPool.class.getName());
    result.put("org.quartz.threadPool.threadCount", String.valueOf(config.getThreadCount()));
    result.put("org.quartz.scheduler.instanceName", config.getSchedulerName());
    result.put("org.quartz.jobStore.misfireThreshold", String.valueOf(config.getMisfireThreshold()));
    return result;
  }

  @Override
  public String getSchedulerName() {
    return schedulerName;
  }

  @Override
  public void start() {
    lock.lock();
    try {
      if (!scheduler.isStarted()) {
        scheduler.start();
      }
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void startDelayed(int seconds) {
    try {
      scheduler.startDelayed(seconds);
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public void stop() {
    try {
      scheduler.shutdown();
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void addJob(JobInfo jobInfo) {
    Class<? extends Job> jobClass = (Class<? extends Job>) JobTypeManager.INSTANCE.getJobClass(jobInfo.getType());
    JobDetail jobDetail = JobBuilder.newJob(jobClass)
        .withIdentity(jobInfo.getJobName(), jobInfo.getGroupName())
        .setJobData(new JobDataMap(jobInfo.getJobData()))
        .build();
    // 添加job data
    // jobDetail.getJobDataMap().put("", "");
    /*Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobInfo.getJobName(), jobInfo.getGroupName())
        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3))
        .withPriority(jobInfo.getPriority().getValue())
        .build();*/

    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobInfo.getJobName(), jobInfo.getGroupName())
        .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCron()))
        .withPriority(jobInfo.getPriority().getValue())
        .build();

    //scheduler.addJob(jobDetail, true);
    try {
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public boolean deleteJob(JobInfo jobInfo) {
    try {
      if (scheduler.checkExists(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()))) {
        return scheduler.deleteJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
      }
      return false;
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public boolean deleteJobs(List<JobInfo> jobInfoList) {
    boolean result = true;
    for (JobInfo jobInfo : jobInfoList) {
      result = deleteJob(jobInfo);
    }
    return result;
  }

  @Override
  public boolean checkExists(JobInfo jobInfo) {
    try {
      return scheduler.checkExists(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public void pauseJob(JobInfo jobInfo) {
    try {
      scheduler.pauseJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public boolean isPaused(JobInfo jobInfo) {
    try {
      return scheduler.isShutdown() && Trigger.TriggerState.PAUSED == scheduler.getTriggerState(new TriggerKey(jobInfo.getJobName(), jobInfo.getGroupName()));
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public void clear() {
    try {
      scheduler.clear();
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public void resumeJob(JobInfo jobInfo) {
    try {
      scheduler.resumeJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public void triggerJob(JobInfo jobInfo) {
    try {
      scheduler.triggerJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getGroupName()));
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

 /* @Override
  public int getCurrentlyExecutingJobs() throws Exception {
    return scheduler.getCurrentlyExecutingJobs().size();
  }*/

  @Override
  public boolean isShutdown() {
    try {
      return scheduler.isShutdown();
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

  @Override
  public boolean isStarted() {
    try {
      return scheduler.isStarted();
    } catch (SchedulerException e) {
      throw new TomgsSchedulerException(e);
    }
  }

}
