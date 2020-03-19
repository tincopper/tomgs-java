package com.tomgs.core.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tangzy
 * @since 1.0
 */
public class TestMapAndFlatMap {

  public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("a a");
    list.add("b b");
    list.add("c c");

    List<String[]> collect = list.stream().map(e -> e.split(" ")).collect(Collectors.toList());
    // [[a, a], [b, b], [c, c]] 这里返回的还是一个字符数组
    System.out.println(collect.toString());
    // 看到这个返回结果的类型是与map里面元素操作返回的结果类型是一样的
    List<Stream<String>> collect2 = list.stream().map(e -> Arrays.stream(e.split(" ")))
        .collect(Collectors.toList());

    List<String> collect1 = list.stream().flatMap(e -> Arrays.stream(e.split(" ")))
        .collect(Collectors.toList());
    // [a, a, b, b, c, c] 这里返回的是字符
    // 看到这个返回结果的类型是与flatMap里面元素lambada操作返回的结果需要的是一个Stream类型，否则不能执行，然后其输出结果进行了扁平化的操作
    System.out.println(collect1.toString());

  }

}
