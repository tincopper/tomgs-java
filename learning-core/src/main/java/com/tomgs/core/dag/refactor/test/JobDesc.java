package com.tomgs.core.dag.refactor.test;

import lombok.Data;

import java.util.Date;

@Data
public class JobDesc {

  private Long id;

  private Long hashId;

  private String jobCategory;

  private String groupName;

  private String jobName;

  private Integer jobType;

  private String jobPath;

  private Integer jobCommandType;

  private String jobCommand;

  private Integer priority;

  private Boolean isDependence;

  private String cron;

  private Integer shardingCount;

  private String jobParameters;

  private String extensibleParameters;

  private Boolean failover;

  private Boolean misfire;

  private Boolean fireNow;

  private Boolean isOnce;

  private Boolean isBroadcast;

  private String slaveIp;

  private Integer timeout;

  private Date endTime;

  private Integer retryCount;

}
