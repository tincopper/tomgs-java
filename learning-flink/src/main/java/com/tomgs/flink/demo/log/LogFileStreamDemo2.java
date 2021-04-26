package com.tomgs.flink.demo.log;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tomgs.flink.demo.log.model.AggResult;
import com.tomgs.flink.demo.log.model.AlarmRule;
import com.tomgs.flink.demo.log.model.AlertEvent;
import com.tomgs.flink.demo.log.model.LogEvent;
import com.tomgs.flink.demo.log.model.RuleMatchedLogEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author tomgs
 * @since 2021/4/21
 */
public class LogFileStreamDemo2 {

  private static Map<String, List<AlarmRule>> alarmRulesMap = new HashMap<>();

  public static void main(String[] args) throws Exception {
    alarmRulesMap.put("jdy_retail_litepos_v7",
        Lists.newArrayList(AlarmRule.builder()
            .id("123")
            .alarmName("test")
            .appName("jdy_retail_litepos_v7")
            .alarmType("AGG")
            .matchCondition("level == 'Info'")
            .alertCondition("count(result) > 1")
            .build()));

    alarmRulesMap.put("jdy_retail_smart_v7",
        Lists.newArrayList(AlarmRule.builder()
            .id("234")
            .alarmName("test1")
            .appName("jdy_retail_smart_v7")
            .alarmType("REAL_TIME")
            .matchCondition("level == 'Info'")
            .alertCondition("result == true")
            .build()));

    /*
      按应用分组、按规则分流
     */
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    String filePath = "E:\\workspace\\tomgs-java\\learning-flink\\src\\main\\resources\\test.log";
//    String filePath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("test.log")).getPath();
    DataStreamSource<String> stream = env.readTextFile(filePath);
    // 转换为json object
    SingleOutputStreamOperator<LogEvent> logEventStream = stream
        .filter(StrUtil::isNotBlank)
        .map(new MapFunction<String, LogEvent>() {
          @Override
          public LogEvent map(String value) {
            JSONObject jsonObject = JSONUtil.parseObj(value);
            return LogEvent.builder().appName(jsonObject.getStr("appname")).message(jsonObject).build();
          }
        });

    // map.keyBy(e -> e.getStr("group") + e.getStr("appname")) // 按 【组+应用名】 分组
    SingleOutputStreamOperator<RuleMatchedLogEvent> rulesStream = logEventStream
        .keyBy(LogEvent::getAppName) // 按应用分组
        .flatMap(new FlatMapFunction<LogEvent, RuleMatchedLogEvent>() {
          @Override
          public void flatMap(LogEvent logEvent, Collector<RuleMatchedLogEvent> out) {
            // 转换成按告警规则的key, value
            String appName = logEvent.getAppName();
            // 获取告警规则
            List<AlarmRule> alarmRules = alarmRulesMap.get(appName);
            if (alarmRules == null) {
              return;
            }
            // 根据规则进行匹配
            List<RuleMatchedLogEvent> messageBeanList = RuleEngine.matcherRules(alarmRules, logEvent);
            // ...
            messageBeanList.forEach(out::collect);
          }
        });

    // 按告警规则分流
    OutputTag<RuleMatchedLogEvent> realtimeAlertStream = new OutputTag<RuleMatchedLogEvent>("realtimeAlertStream") {};
    OutputTag<RuleMatchedLogEvent> aggAlertStream = new OutputTag<RuleMatchedLogEvent>("aggAlertStream") {};

    SingleOutputStreamOperator<RuleMatchedLogEvent> processStream = rulesStream.process(
        new ProcessFunction<RuleMatchedLogEvent, RuleMatchedLogEvent>() {
          @Override
          public void processElement(RuleMatchedLogEvent event, Context ctx, Collector<RuleMatchedLogEvent> out) {
            if ("AGG".equals(event.getAlarmRule().getAlarmType())) {
              ctx.output(aggAlertStream, event);
              return;
            }
            ctx.output(realtimeAlertStream, event);
          }
        });

    DataStream<RuleMatchedLogEvent> realtimeAlertSideOutput = processStream.getSideOutput(realtimeAlertStream);
    DataStream<RuleMatchedLogEvent> aggAlertSideOutput = processStream.getSideOutput(aggAlertStream);

    // 处理实时数据
    SingleOutputStreamOperator<AlertEvent> realtimeAlert = realtimeAlertSideOutput
        .keyBy(RuleMatchedLogEvent::getRuleId) // 按规则进行聚合
        .filter(RuleEngine::alertJudge)
        .map(new MapFunction<RuleMatchedLogEvent, AlertEvent>() {
          @Override
          public AlertEvent map(RuleMatchedLogEvent event) {
            return AlertEvent.builder()
                .appName(event.getAlarmRule().getAppName())
                .message(event.getLogEvent().toString())
                .alarmRule(event.getAlarmRule())
                .alarmResult(event.getMatchedResult())
                .build();
          }
        });

    // 处理聚合数据
    SingleOutputStreamOperator<AlertEvent> aggAlert = aggAlertSideOutput
        .keyBy(RuleMatchedLogEvent::getRuleId)
        .process(new AggWindowFunction())
        .map(new MapFunction<AggResult, AlertEvent>() {
          @Override
          public AlertEvent map(AggResult event) {
            return AlertEvent.builder()
                .appName(event.getAlarmRule().getAppName())
                .message(event.toString())
                .alarmRule(event.getAlarmRule())
                .alarmResult(event.getMatchedResult())
                .build();
          }
        });

    // 聚合处理之后的数据处理，对聚合之后的流进行两次处理
    // 1、聚合告警
    // 2、输出到存储做展示

    realtimeAlert
        .union(aggAlert)
//        .map(new AlertJudgeMapFunction())
        .addSink(new AlertSink());

    env.execute("log file demo");
  }

}
