package com.tomgs.flink.demo.datastreamapi;

import java.util.List;
import java.util.Map;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 复杂事件处理 CEP
 *
 * CEP, is event processing that combines data from multiple sources to infer events or patterns that suggest more complicated circumstances.
 * The goal of complex event processing is to identify meaningful events (such as opportunities or threats) and respond to them as quickly as possible.
 *
 * @author tomgs
 * @since 2021/4/25
 */
public class FlinkCEPDemo {

  public static void main(String[] args) throws Exception{

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    DataStreamSource<Tuple3<String, String, Long>> source = env.fromElements(
        //浏览记录
        Tuple3.of("Marry", "外套", 1L),
        Tuple3.of("Marry", "帽子",1L),
        Tuple3.of("Marry", "帽子",2L),
        Tuple3.of("Marry", "帽子",3L),
        Tuple3.of("Ming", "衣服",1L),
        Tuple3.of("Marry", "鞋子",1L),
        Tuple3.of("Marry", "鞋子",2L),
        Tuple3.of("LiLei", "帽子",1L),
        Tuple3.of("LiLei", "帽子",2L),
        Tuple3.of("LiLei", "帽子",3L)
    );

    //定义Pattern,寻找连续搜索帽子的用户
    Pattern<Tuple3<String, String, Long>, Tuple3<String, String, Long>> pattern = Pattern
        .<Tuple3<String, String, Long>>begin("start")
        .where(new SimpleCondition<Tuple3<String, String, Long>>() {
          @Override
          public boolean filter(Tuple3<String, String, Long> value) throws Exception {
            return value.f1.equals("帽子");
          }
        }) //.timesOrMore(3);
        .next("middle")
        .where(new SimpleCondition<Tuple3<String, String, Long>>() {
          @Override
          public boolean filter(Tuple3<String, String, Long> value) throws Exception {
            return value.f1.equals("帽子");
          }
        });


    KeyedStream keyedStream = source.keyBy(0);
    PatternStream patternStream = CEP.pattern(keyedStream, pattern);
    SingleOutputStreamOperator matchStream = patternStream.select(new PatternSelectFunction<Tuple3<String, String, Long>, String>() {

      @Override
      public String select(Map<String, List<Tuple3<String, String, Long>>> pattern) throws Exception {
        List<Tuple3<String, String, Long>> middle = pattern.get("middle");
        return middle.get(0).f0 + ":" + middle.get(0).f2 + ":" + "连续搜索两次帽子!";
      }

    });

    matchStream.printToErr();
    env.execute("execute cep");
  }

}
