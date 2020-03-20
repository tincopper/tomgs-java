package com.tomgs.scheduler.quartz.customer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzy
 * @since 1.0
 */
public class DemoJob implements JobInfo {

  @Override
  public String getJobKey() {
    return getJobName() + getGroupName();
  }

  @Override
  public String getGroupName() {
    return "initJob";
  }

  @Override
  public String getJobName() {
    return "initJob";
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

}
