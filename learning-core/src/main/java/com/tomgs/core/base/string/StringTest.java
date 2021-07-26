package com.tomgs.core.base.string;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/19 1.0 
 */
@BenchmarkMode(Mode.Throughput) // 吞吐量
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
@Fork(2) // Fork进行的数目
@Warmup(iterations = 4) // 先预热4轮
@Measurement(iterations = 10) // 进行10轮测试
public class StringTest {

    @Test
    public void testReverse() {
        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        StringBuilder reverse = sb.reverse();
        System.out.println(reverse.toString());
    }

    @Test
    public void testSplit() {
        // split
        // StringTokenizer
        // CustomerSplit
    }

}
