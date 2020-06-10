package com.tomgs.scheduler.quartz.customer;

import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import com.tomgs.scheduler.quartz.customer.extension.SPI;
import java.util.List;

@SPI("scheduler")
public interface BasicScheduler {

  void config(SchedulerConfig config) throws Exception;

  void start() throws Exception;

  void startDelayed(int seconds) throws Exception;

  void stop() throws Exception;

  void addJob(JobInfo jobInfo) throws Exception;

  boolean deleteJob(JobInfo jobInfo) throws Exception;

  boolean deleteJobs(List<JobInfo> jobInfoList) throws Exception;

  boolean checkExists(JobInfo jobInfo) throws Exception;

  void pauseJob(JobInfo jobInfo) throws Exception;

  boolean isPaused(JobInfo jobInfo) throws Exception;

  void clear() throws Exception;

  void resumeJob(JobInfo jobInfo) throws Exception;

  void triggerJob(JobInfo jobInfo) throws Exception;

  //int getCurrentlyExecutingJobs() throws Exception;

  boolean isShutdown() throws Exception;

  boolean isStarted() throws Exception;

}
