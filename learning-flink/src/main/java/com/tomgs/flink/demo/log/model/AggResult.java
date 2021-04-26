package com.tomgs.flink.demo.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tomgs
 * @since 2021/4/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggResult {

  private String ruleId;

  private AlarmRule alarmRule;

  private Object matchedResult;

  private LogEvent logEvent;

  private SummaryStatistics statistics;

}
