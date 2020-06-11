package com.tomgs.scheduler.quartz.customer;

import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import com.tomgs.scheduler.quartz.customer.extension.SPI;
import java.util.List;

@SPI("scheduler")
public interface BasicScheduler {

  String getSchedulerName();

  void config(SchedulerConfig config);

  void start();

  void startDelayed(int seconds);

  void stop();

  void addJob(JobInfo jobInfo);

  boolean deleteJob(JobInfo jobInfo);

  boolean deleteJobs(List<JobInfo> jobInfoList);

  boolean checkExists(JobInfo jobInfo);

  void pauseJob(JobInfo jobInfo);

  boolean isPaused(JobInfo jobInfo);

  void clear();

  void resumeJob(JobInfo jobInfo);

  void triggerJob(JobInfo jobInfo);

  //int getCurrentlyExecutingJobs();

  boolean isShutdown();

  boolean isStarted();

}
