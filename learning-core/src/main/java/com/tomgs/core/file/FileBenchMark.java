package com.tomgs.core.file;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * @author tomgs
 * @date 2021/7/26 10:34
 * @since 1.0
 */
@BenchmarkMode(Mode.SampleTime) // 吞吐量
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
@Fork(1) // Fork进行的数目
@Warmup(iterations = 1) // 先预热1轮
@Measurement(iterations = 3) // 进行1轮测试
public class FileBenchMark {

    @Benchmark
    public void testNoFileChannel() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("E:\\workspace\\hodor\\logs\\hodor-scheduler_2021-08-25_2.log.gz"));
        GZIPInputStream gis = new GZIPInputStream(fileInputStream);

        BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
        String readLine;
        while ((readLine = reader.readLine()) != null) {
            //System.out.println(readLine);
        }
    }

    @Benchmark
    public void testFileChannel() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("E:\\workspace\\hodor\\logs\\hodor-scheduler_2021-08-25_2.log.gz"));
        FileChannel fc = fileInputStream.getChannel();
        GZIPInputStream gis = new GZIPInputStream(Channels.newInputStream(fc));

        BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
        String readLine;
        while ((readLine = reader.readLine()) != null) {
            //System.out.println(readLine);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(FileBenchMark.class.getSimpleName()).build();
        new Runner(options).run();
    }

}