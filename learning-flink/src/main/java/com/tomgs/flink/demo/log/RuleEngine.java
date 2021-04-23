package com.tomgs.flink.demo.log;

import cn.hutool.json.JSONObject;
import com.tomgs.flink.demo.log.model.AlarmRule;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * rule engine
 *
 * @author tomgs
 * @since 2021/4/22
 */
public class RuleEngine {

  public static List<JSONObject> matcherRules(List<AlarmRule> alarmRules, Tuple2<String, JSONObject> message) {
    return alarmRules.stream().map(rule -> {
      // 匹配数据
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("ruleId", rule.getId());
      jsonObject.put("ruleType", rule.getAlarmType());
      jsonObject.put("ruleMessage", message.f1);
      jsonObject.put("appname", rule.getAppName());
      // handle rule
      if (message.f1.getStr("message").contains(rule.getAlarmName())) {
        jsonObject.put("alarmMessage", message.f1.getStr("message"));
      }
      return jsonObject;
    }).collect(Collectors.toList());
  }

}
