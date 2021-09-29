package com.tomgs.core.benchmark;

import cn.hutool.core.text.StrSplitter;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * @author tomgs
 * @date 2021/7/26 10:52
 * @since 1.0
 */
@BenchmarkMode(Mode.Throughput) // 吞吐量
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
@Fork(1) // Fork进行的数目
@Warmup(iterations = 1) // 先预热2轮
@Measurement(iterations = 1) // 进行2轮测试
public class BenchMarkSplit {

    @Param({"/a/b", "/a/b/c", "/a/b/c/d"})
    private String str;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchMarkSplit.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @Benchmark
    public void strSplit() {
        str.split("/", 3);
    }

    @Benchmark
    public void strTokenizer() {
        StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
        while (stringTokenizer.hasMoreTokens()) {
            stringTokenizer.nextToken();
        }
    }

    @Benchmark
    public void stringAnalytical() {
        stringAnalytical(str, '/');
    }

    @Benchmark
    public void customSplit() {
        split(str, "/");
    }

    @Benchmark
    public void stringUtils() {
        StringUtils.split(str, "/");
    }

    private final Splitter splitter = Splitter.on("/");
            //.limit(3);
    @Benchmark
    public void guavaSplitter() {
        splitter.split(str);
        //splitter.splitToList(str); // 这个性能比较低
    }

    @Benchmark
    public void hutoolSplit() {
        StrSplitter.split(str, 3);
    }

    // indexOf和subString的性能比较高
    private String[] split(String str, String delim) {
        int i = str.indexOf(delim);
        if (i < 0) {
            return new String[]{str};
        }
        String firstStr = str.substring(0, i);
        String lastStr = str.substring(i + 1);
        i = lastStr.indexOf(delim);
        if (i <= 0) {
            return new String[]{firstStr, lastStr};
        }
        List<String> list = new ArrayList<>();
        list.add(firstStr);
        while (i > 0) {
            list.add(lastStr.substring(0, i));
            lastStr = lastStr.substring(i + 1);
            i = lastStr.indexOf(delim);
        }
        list.add(lastStr);
        return list.toArray(new String[]{});
    }

    private String[] stringAnalytical(String string, char c) {
        int i = 0;
        int count = 0;
        if (string.indexOf(c) == -1)
            return new String[]{string};// 如果不含分割符则返回字符本身
        char[] cs = string.toCharArray();
        int length = cs.length;
        for (i = 1; i < length - 1; i++) {// 过滤掉第一个和最后一个是分隔符的情况
            if (cs[i] == c) {
                count++;// 得到分隔符的个数
            }
        }
        String[] strArray = new String[count + 1];
        int k = 0, j = 0;
        String str = string;
        if ((k = str.indexOf(c)) == 0)// 去掉第一个字符是分隔符的情况
            str = str.substring(k + 1);
        if (str.indexOf(c) == -1)// 检测是否含分隔符，如果不含则返回字符串
            return new String[]{str};
        while ((k = str.indexOf(c)) != -1) {// 字符串含分割符的时候
            strArray[j++] = str.substring(0, k);
            str = str.substring(k + 1);
            if ((k = str.indexOf(c)) == -1 && str.length() > 0)
                strArray[j++] = str.substring(0);
        }
        return strArray;
    }

}
