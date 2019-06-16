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
 *    ①Stream 自己不会存储元素。
 *    ②Stream 不会改变源对象。相反，他们会返回一个持有结果的新Stream。
 *    ③Stream 操作是延迟执行的。这意味着他们会等到需要结果的时候才执行。
 *
 *  2、中间操作
 *    中间操作不会执行任何的处理！而在终止操作时一次性全部处理，称为“惰性求值”
 *
 *  3、终止操作
 *
 *
 * @author tomgs
 * @version 2019/6/8 1.0 
 */
public class TestStreamAPI2 {

    //1、stream的中间操作
    @Test
    public void testCreateStream() {

    }
}
