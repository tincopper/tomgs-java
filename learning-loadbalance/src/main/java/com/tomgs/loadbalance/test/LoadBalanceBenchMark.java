package com.tomgs.loadbalance.test;

import com.google.common.collect.Lists;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tomgs
 * @date 2021/7/26 10:34
 * @since 1.0
 */
@BenchmarkMode(Mode.Throughput) // 吞吐量
@OutputTimeUnit(TimeUnit.SECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
@Fork(1) // Fork进行的数目
@Warmup(iterations = 1) // 先预热1轮
@Measurement(iterations = 3) // 进行1轮测试
public class LoadBalanceBenchMark {

    private List<ServerInfo> servers;

    private SmoothRoundRobinLoadBalance2 loadBalance;

    @Setup(Level.Trial) // 初始化方法，在全部Benchmark运行之前进行
    public void init() {
        ServerInfo serverInfo1 = ServerInfo.builder().host("1").weight(5).build();
        ServerInfo serverInfo2 = ServerInfo.builder().host("2").weight(2).build();
        ServerInfo serverInfo3 = ServerInfo.builder().host("3").weight(3).build();
        ServerInfo serverInfo4 = ServerInfo.builder().host("4").weight(10).build();
        ServerInfo serverInfo5 = ServerInfo.builder().host("5").weight(10).build();
        servers = Lists.newArrayList(serverInfo1, serverInfo2, serverInfo3, serverInfo4, serverInfo5);

        loadBalance = new SmoothRoundRobinLoadBalance2();
    }

    @Benchmark
    public void smoothRoundRobingLoadBalanceBenchMark() {
        // 42790876.175 ± 3741821.438  ops/s
        loadBalance.doSelect(servers);
    }

    @TearDown(Level.Trial) // 结束方法，在全部Benchmark运行之后进行
    public void clear() {
        servers.clear();
        loadBalance = null;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(LoadBalanceBenchMark.class.getSimpleName()).build();
        new Runner(options).run();
    }

}