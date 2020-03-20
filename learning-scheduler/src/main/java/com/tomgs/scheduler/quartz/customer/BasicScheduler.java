package com.tomgs.scheduler.quartz.customer;

public interface BasicScheduler {

  void config() throws Exception;

  void start() throws Exception;

  void startDelayed(int seconds) throws Exception;

  void stop() throws Exception;

  void addJob(JobInfo jobInfo) throws Exception;

  boolean deleteJob(JobInfo jobInfo) throws Exception;

  boolean checkExists(JobInfo jobInfo) throws Exception;

  void pauseJob(JobInfo jobInfo) throws Exception;

  void clear() throws Exception;

  void resumeJob(JobInfo jobInfo) throws Exception;

  void triggerJob(JobInfo jobInfo) throws Exception;

  int getCurrentlyExecutingJobs() throws Exception;

  boolean isShutdown() throws Exception;

  boolean isStarted() throws Exception;

}
