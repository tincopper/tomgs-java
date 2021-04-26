package com.tomgs.flink.demo.log;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.tomgs.flink.demo.log.model.AlarmRule;
import com.tomgs.flink.demo.log.model.LogEvent;
import com.tomgs.flink.demo.log.model.RuleMatchedLogEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * rule engine
 *
 * @author tomgs
 * @since 2021/4/22
 */
@Slf4j
public class RuleEngine {

  /**
   * 匹配规则
   */
  public static List<RuleMatchedLogEvent> matcherRules(List<AlarmRule> alarmRules, LogEvent logEvent) {
    List<RuleMatchedLogEvent> matchedResults = new ArrayList<>();
    for (AlarmRule rule : alarmRules) {
      Object matchResult = null;
      if (StringUtils.isNotBlank(rule.getMatchCondition())) {
        matchResult = AviatorEvaluator.execute(rule.getMatchCondition(), logEvent.getMessage(), true);
      }
      if (matchResult == null) {
        continue;
      }
      // 匹配数据
      matchedResults.add(RuleMatchedLogEvent.builder()
          .ruleId(rule.getId())
          .logEvent(logEvent)
          .alarmRule(rule)
          .matchedResult(matchResult)
          .build());
    }
    return matchedResults;
  }

  public static boolean alertJudge(RuleMatchedLogEvent event) {
    AlarmRule alarmRule = event.getAlarmRule();
    String alertCondition = alarmRule.getAlertCondition();
    if (StringUtils.isBlank(alertCondition)) {
      return false;
    }

    Object matchedResult = event.getMatchedResult();
    if (matchedResult instanceof Boolean && !(boolean) matchedResult) {
      return false;
    }

    JSONObject message = event.getLogEvent().getMessage();
    message.put("result", event.getMatchedResult());
    try {
      Object result = AviatorEvaluator.execute(alertCondition, message, true);
      if (result instanceof Boolean) {
        return (boolean) result;
      }
    } catch (Exception e) {
      log.error("execute alert condition exception, alertCondition: {}, msg: {}.", alertCondition, e.getMessage(), e);
    }
    return false;
  }

  public static boolean alertAggJudge() {

    return false;
  }

}
