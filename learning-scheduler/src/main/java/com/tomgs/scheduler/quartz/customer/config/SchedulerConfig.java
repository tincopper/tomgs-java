package com.tomgs.scheduler.quartz.customer.config;

/**
 * scheduler config
 * @author tangzy
 */
public class SchedulerConfig {

  private String schedulerName;
  private int threadCount;
  private int misfireThreshold;

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public String getSchedulerName() {
    return schedulerName;
  }

  public void setSchedulerName(String schedulerName) {
    this.schedulerName = schedulerName;
  }

  public int getMisfireThreshold() {
    return misfireThreshold;
  }

  public void setMisfireThreshold(int misfireThreshold) {
    this.misfireThreshold = misfireThreshold;
  }

}
