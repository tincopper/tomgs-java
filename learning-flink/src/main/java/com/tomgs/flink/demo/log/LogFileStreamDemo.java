package com.tomgs.flink.demo.log;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tomgs.flink.demo.log.model.AlarmRule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author tomgs
 * @since 2021/4/21
 */
public class LogFileStreamDemo {

  private static Map<String, List<AlarmRule>> alarmRulesMap = new HashMap<>();

  public static void main(String[] args) throws Exception {
    alarmRulesMap.put("jdy_retail_litepos_v7",
        Lists.newArrayList(AlarmRule.builder()
            .id("123")
            .alarmName("test")
            .appName("jdy_retail_litepos_v7")
            .alarmType("AGG")
            .build()));

    alarmRulesMap.put("jdy_retail_smart_v7",
        Lists.newArrayList(AlarmRule.builder()
            .id("234")
            .alarmName("test1")
            .appName("jdy_retail_smart_v7")
            .alarmType("REAL_TIME")
            .build()));

    /*
      按应用分组、按规则分流
     */
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    String filePath = "E:\\workspace\\tomgs-java\\learning-flink\\src\\main\\resources\\test.log";
//    String filePath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("test.log")).getPath();
    DataStreamSource<String> stream = env.readTextFile(filePath);
    // 转换为json object
    SingleOutputStreamOperator<Tuple2<String, JSONObject>> map = stream
        .filter(StrUtil::isNotBlank)
        .map(new MapFunction<String, Tuple2<String, JSONObject>>() {
          @Override
          public Tuple2<String, JSONObject> map(String value) throws Exception {
            JSONObject jsonObject = JSONUtil.parseObj(value);
            return new Tuple2<>(jsonObject.getStr("appname"), jsonObject);
          }
        });

    // map.keyBy(e -> e.getStr("group") + e.getStr("appname")) // 按 【组+应用名】 分组
    SingleOutputStreamOperator<JSONObject> rulesStream = map
        .keyBy(e -> e.f0) // 按应用分组
        .flatMap(new FlatMapFunction<Tuple2<String, JSONObject>, JSONObject>() {
          @Override
          public void flatMap(Tuple2<String, JSONObject> message, Collector<JSONObject> out) throws Exception {
            // 转换成按告警规则的key, value
            String appName = message.f0;
            // 获取告警规则
            List<AlarmRule> alarmRules = alarmRulesMap.get(appName);
            if (alarmRules == null) {
              return;
            }
            // 根据规则进行匹配
            List<JSONObject> messageBeanList = RuleEngine.matcherRules(alarmRules, message);
            // ...
            messageBeanList.forEach(out::collect);
          }

        });

    // 按告警规则分流
    OutputTag<Tuple2<String, JSONObject>> realtimeAlertStream = new OutputTag<Tuple2<String, JSONObject>>("realtimeAlertStream") {};
    OutputTag<Tuple2<String, JSONObject>> aggAlertStream = new OutputTag<Tuple2<String, JSONObject>>("aggAlertStream") {};

    SingleOutputStreamOperator<Tuple2<String, JSONObject>> processStream = rulesStream.process(
        new ProcessFunction<JSONObject, Tuple2<String, JSONObject>>() {
          @Override
          public void processElement(JSONObject value, Context ctx, Collector<Tuple2<String, JSONObject>> out) throws Exception {
            if ("AGG".equals(value.getStr("ruleType"))) {
              ctx.output(aggAlertStream, new Tuple2<>(value.getStr("ruleId"), value));
              return;
            }
            ctx.output(realtimeAlertStream, new Tuple2<>(value.getStr("ruleId"), value));
          }
        });

    DataStream<Tuple2<String, JSONObject>> realtimeAlertSideOutput = processStream.getSideOutput(realtimeAlertStream);
    DataStream<Tuple2<String, JSONObject>> aggAlertSideOutput = processStream.getSideOutput(aggAlertStream);

    // 处理实时数据
    SingleOutputStreamOperator<String> realtimeAlert = realtimeAlertSideOutput
        .keyBy(message -> message.f0) // 按规则进行聚合
        .process(new KeyedProcessFunction<String, Tuple2<String, JSONObject>, String>() {
          @Override
          public void processElement(Tuple2<String, JSONObject> value, Context ctx, Collector<String> out) throws Exception {
            out.collect(value.f1.toString());
          }
        })
        .map(new MapFunction<String, String>() {
          @Override
          public String map(String value) throws Exception {
            return value;
          }
        });

    // 处理聚合数据
    SingleOutputStreamOperator<String> aggAlert = aggAlertSideOutput
        .keyBy(message -> message.f0)
        .process(new AggWindowFunction())
        .map(new MapFunction<String, String>() {
          @Override
          public String map(String value) throws Exception {
            return value;
          }
        });

    // 聚合处理之后的数据处理，对聚合之后的流进行两次处理
    // 1、聚合告警
    // 2、输出到存储做展示

    realtimeAlert
        .union(aggAlert)
        .map(new AlertJudgeMapFunction())
        .addSink(new AlertSink());

    env.execute("log file demo");
  }

}
