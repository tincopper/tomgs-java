package com.tomgs.flink.demo.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据规则匹配的日志事件
 *
 * @author tomgs
 * @since 2021/4/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleMatchedLogEvent {

  private String ruleId;

  private AlarmRule alarmRule;

  private LogEvent logEvent;

  private Object matchedResult;

}
