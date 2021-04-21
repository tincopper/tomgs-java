package com.tomgs.flink.demo.log;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
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
public class LogFileStreamDemo {

  private static Map<String, List<JSONObject>> alarmRulesMap = new HashMap<>();

  public static void main(String[] args) throws Exception {
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
        .map(e -> {
          JSONObject jsonObject = JSONUtil.parseObj(e);
          return new Tuple2<>(jsonObject.getStr("appname"), jsonObject);
        });

    // map.keyBy(e -> e.getStr("group") + e.getStr("appname")) // 按 【组+应用名】 分组
    map.keyBy(e -> e.f0) // 按应用分组
        .flatMap(new FlatMapFunction<Tuple2<String, JSONObject>, Tuple2<String, JSONObject>>() {
          @Override
          public void flatMap(Tuple2<String, JSONObject> value, Collector<Tuple2<String, JSONObject>> out) throws Exception {
           // 转换成按告警规则的key, value
            String appName = value.f0;
            JSONObject data = value.f1;
            // 获取告警规则
            List<JSONObject> alarmRules = alarmRulesMap.get(appName);
            // ...
            out.collect(new Tuple2<>(appName, data));
          }
        });

    // 分流
    OutputTag<Tuple2<String, JSONObject>> realtimeStream = new OutputTag<Tuple2<String, JSONObject>>("realtimeStream") {};
    OutputTag<Tuple2<String, JSONObject>> aggStream = new OutputTag<Tuple2<String, JSONObject>>("aggStream") {};

    SingleOutputStreamOperator<Tuple2<String, JSONObject>> process = map.process(
        new ProcessFunction<Tuple2<String, JSONObject>, Tuple2<String, JSONObject>>() {
          @Override
          public void processElement(Tuple2<String, JSONObject> value, Context ctx, Collector<Tuple2<String, JSONObject>> out) throws Exception {
            ctx.output(realtimeStream, value);
            ctx.output(aggStream, value);
          }
        });

    env.execute("log file demo");
  }

}
