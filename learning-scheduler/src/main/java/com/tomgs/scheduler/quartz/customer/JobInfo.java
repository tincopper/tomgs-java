package com.tomgs.scheduler.quartz.customer;

import java.util.Map;

public interface JobInfo {

  String getJobKey();

  String getGroupName();

  String getJobName();

  int getType();

  Priority getPriority();

  Map<String, Object> getJobData();

}
