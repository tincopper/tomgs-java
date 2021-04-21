package com.tomgs.flink.demo.log;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * 日志处理
 *
 * @author tomgs
 * @since 2021/4/21
 */
public class LogStreamDemo {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    Properties prop = new Properties();
    prop.setProperty("bootstrap.servers","172.20.182.13:9092,172.20.182.14:9092");
    prop.setProperty("group.id","logstash");

    FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>("topic-jdyapp-log", new SimpleStringSchema(), prop);
    //env.getConfig().disableSysoutLogging();
    env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(5,5000));
    env.enableCheckpointing(2000);
    DataStreamSource<String> stream = env.addSource(consumer);
    DataStream<String> sourceStream = stream
        .filter((FilterFunction<String>) StringUtils::isNotBlank)
        .map((MapFunction<String, String>) value -> {
          //MonitorData monitorData = JSONObject.parseObject(value, MonitorData.class);
          System.out.println(value);
          return value;
        });

    //把数据打印到控制台//使用一个并行度
    sourceStream.print().setParallelism(1);

    env.execute("kafka data test");
  }

}
