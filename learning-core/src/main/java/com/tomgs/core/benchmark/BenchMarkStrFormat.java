package com.tomgs.core.benchmark;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Strings;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * 字符串格式化压测
 *
 * @author tomgs
 * @since 2021/7/29
 */
@BenchmarkMode(Mode.Throughput) // 吞吐量
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
@Fork(1) // Fork进行的数目
@Warmup(iterations = 1) // 先预热2轮
@Measurement(iterations = 1) // 进行2轮测试
public class BenchMarkStrFormat {

    @Param({"/a/%s/c/%s"})
    private String format;

    @Param({"/a/{0}/c/{1}"})
    private String text;

    @Param({"/a/{}/c/{}"})
    private String hutool;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchMarkStrFormat.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @Benchmark
    public void javaFormat() {
        String.format(format, "1", "2");
    }

    @Benchmark
    public void javaMessageFormat() {
        MessageFormat.format(text, "1", "2");
    }

    @Benchmark
    public void guavaFormat() {
        Strings.lenientFormat(format, "1", "2");
    }

    @Benchmark
    public void hutoolFormat() {
        StrUtil.format(hutool, "1", "2");
    }

}
