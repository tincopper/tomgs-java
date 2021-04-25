package com.tomgs.flink.demo.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tomgs
 * @since 2021/4/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertEvent {

  private String appName;

  private AlarmRule alarmRule;

  private String message;

  private Object alarmResult;

}
