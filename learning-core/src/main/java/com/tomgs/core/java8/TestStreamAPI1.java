package com.tomgs.core.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 *  StreamAPI学习
 *
 *  1、创建stream
 *  2、中间操作
 *  3、终止操作
 *
 *  ①Stream 自己不会存储元素。
 *  ②Stream 不会改变源对象。相反，他们会返回一个持有结果的新Stream。
 *  ③Stream 操作是延迟执行的。这意味着他们会等到需要结果的时候才执行。
 *
 * @author tomgs
 * @version 2019/6/8 1.0 
 */
public class TestStreamAPI1 {

    //1、创建stream
    @Test
    public void testCreateStream() {
        //1、通过Collection集合提供的stream()或者parallelStream()
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();

        //2、通过Arrays的静态方法stream获取数组流
        String[] strings = new String[10];
        Stream<String> stream1 = Arrays.stream(strings);

        //3、通过Stream的of方法产生
        Stream<String> stream2 = Stream.of("a", "b", "c");

        //4、创建无限流
        //①迭代
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2);
        stream3.limit(10).forEach(System.out::println);

        //②generate
        Stream<Double> generate = Stream.generate(() -> Math.random());
        generate.limit(5).forEach(System.out::println);
    }
}
