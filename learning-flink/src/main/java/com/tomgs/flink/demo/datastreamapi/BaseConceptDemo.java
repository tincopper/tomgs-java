package com.tomgs.flink.demo.datastreamapi;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;
import org.junit.Test;

/**
 * 相关的基本核心概念
 *
 * @author tomgs
 * @since 2021/4/16
 */
public class BaseConceptDemo {

  final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

  /**
   * 故障恢复与重启 Flink 支持了不同级别的故障恢复策略，jobmanager.execution.failover-strategy 的可配置项有两种：full 和 region。
   * 1、故障恢复策略为 full 时，集群中的 Task 发生故障，那么该任务的所有 Task 都会发生重启。 2、故障恢复策略为 region 时，Flink 会把我们的任务分成不同的
   * Region，当某一个 Task 发生故障时，Flink 会计算需要故障恢复的最小 Region
   */
  @Test
  public void restartStrategy() {
    // 不重启，异常直接退出
    //env.setRestartStrategy(RestartStrategies.noRestart());

    // 固定延迟重启
    //env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
    //    3, // 尝试重启的次数
    //    Time.of(10, TimeUnit.SECONDS) // 延时
    //));

    // 失败率重启
    //  5 分钟内若失败了 3 次，则认为该任务失败，每次失败的重试间隔为 5 秒。
    env.setRestartStrategy(RestartStrategies.failureRateRestart(
        3, // 每个时间间隔的最大故障次数
        Time.of(5, TimeUnit.MINUTES), // 测量故障率的时间间隔
        Time.of(5, TimeUnit.SECONDS) // 延时
    ));
  }

  /**
   * 并行度 1、算子级别 2、执行环境级别 3、提交任务级别 4、系统配置级别 优先级从高到低
   * <p>
   * Flink 中的 TaskManager 是执行任务的节点，那么在每一个 TaskManager 里，还会有“槽位”，也就是 Slot。Slot 个数代表的是每一个 TaskManager
   * 的并发执行能力。 Slot 就是资源的最小单位，经验上讲 Slot 的数量与 CPU-core 的数量一致为好。但考虑到超线程，可以 slot=2*cpuCore。
   */
  @Test
  public void parallelism() {
//    env.setParallelism(5); // 执行环境级别

//    DataSet<Tuple2<String, Integer>> counts =
//        text.flatMap(new LineSplitter())
//            .groupBy(0)
//            .sum(1)
//            .setParallelism(1); // 算子级别

//    提交任务级别
//    ./bin/flink run -p 10 WordCount.jar

// flink-conf.yaml 中的一个配置：parallelism.default，该配置即是在系统层面设置所有执行环境的并行度配置。
  }

  @Test
  public void distributeCache() throws Exception {
    env.registerCachedFile("/Users/wangzhiwu/WorkSpace/quickstart/distributedcache.txt",
        "distributedCache");
    //1：注册一个文件,可以使用hdfs上的文件 也可以是本地文件进行测试
    DataSource<String> data = env.fromElements("Linea", "Lineb", "Linec", "Lined");

    DataSet<String> result = data.map(new RichMapFunction<String, String>() {
      private ArrayList<String> dataList = new ArrayList<>();

      @Override
      public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //2：使用该缓存文件
        File myFile = getRuntimeContext().getDistributedCache().getFile("distributedCache");
        List<String> lines = FileUtils.readLines(myFile, StandardCharsets.UTF_8);
        for (String line : lines) {
          this.dataList.add(line);
          System.err.println("分布式缓存为:" + line);
        }
      }

      @Override
      public String map(String value) throws Exception {
        //在这里就可以使用dataList
        System.err.println("使用datalist：" + dataList + "-------" + value);
        //业务逻辑
        return dataList + "：" + value;
      }
    });

    result.printToErr();
  }

}
