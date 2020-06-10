package com.tomgs.scheduler.quartz.customer;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author tangzy
 * @since 1.0
 */
@Data
public class NewJobRequest implements JobInfo {

  private String groupName;

  private String jobName;

  private String cron;

  @Override
  public String getJobKey() {
    return getJobName() + getGroupName();
  }

  @Override
  public String getGroupName() {
    return groupName;
  }

  @Override
  public String getJobName() {
    return jobName;
  }

  @Override
  public int getType() {
    return 0;
  }

  @Override
  public Priority getPriority() {
    return Priority.DEFAULT;
  }

  @Override
  public Map<String, Object> getJobData() {
    return new HashMap<>();
  }

  @Override
  public String getCron() {
    return cron;
  }

}