package com.tomgs.flink.demo.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 告警规则
 *
 * @author tomgs
 * @since 2021/4/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRule {

  private String id;

  private String appName;

  private String alarmName;

  private String alarmType;

  private AlarmMatchMode alarmMatchMode;

  private String matchCondition;

  private String alertCondition;

}
