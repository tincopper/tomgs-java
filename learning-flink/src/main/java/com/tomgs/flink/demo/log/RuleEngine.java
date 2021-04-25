package com.tomgs.flink.demo.log;

import com.googlecode.aviator.AviatorEvaluator;
import com.tomgs.flink.demo.log.model.AlarmRule;
import com.tomgs.flink.demo.log.model.AlertEvent;
import com.tomgs.flink.demo.log.model.LogEvent;
import com.tomgs.flink.demo.log.model.RuleMatchedLogEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.guava18.com.google.common.collect.Maps;

/**
 * rule engine
 *
 * @author tomgs
 * @since 2021/4/22
 */
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

  public static boolean alertJudge(RuleMatchedLogEvent event) throws Exception {
    AlarmRule alarmRule = event.getAlarmRule();

    HashMap<String, Object> env = Maps.newHashMap();
    env.put("result", event.getMatchedResult());

    Object result = AviatorEvaluator.execute(alarmRule.getAlertCondition(), env, true);
    if (!(result instanceof Boolean)) {
      throw new IllegalArgumentException(String.format("告警条件[%s]配置错误", alarmRule.getAlertCondition()));
    }

    return (boolean) result;
  }

}
