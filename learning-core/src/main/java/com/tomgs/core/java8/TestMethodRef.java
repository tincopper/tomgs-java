package com.tomgs.core.java8;

import org.junit.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  java8 方法引用与构造器引用
 *  一、方法引用
 *  对象::实例方法名
 *  类::静态方法名
 *  类::实例方法名
 *
 *  ①Lambda体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致
 *  ②若Lambda参数列表中的第一参数是实例方法的调用者，而第二个参数是实例方法的参数时，可以使用ClassName::method
 *
 *  二、构造器引用
 *  ClassName:new
 *
 *  三、数组引用
 *  Type::new
 *
 * @author tomgs
 * @version 2019/6/8 1.0 
 */
public class TestMethodRef {

    //一、方法引用
    //对象::实例方法名
    @Test
    public void test1() {
        PrintStream ps = System.out;
        Consumer<String> consumer = (x) -> ps.println(x);
        consumer.accept("123");

        Consumer<String> consumer2 = ps::println;
        consumer2.accept("234");
    }

    //类::静态方法名
    @Test
    public void test2() {
        Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
        System.out.println(comparator.compare(1, 2));

        Comparator<Integer> comparator1 = Integer::compare;
        System.out.println(comparator1.compare(1, 2));
    }

    //类::实例方法名
    @Test
    public void test3() {
        BiPredicate<String, String> predicate = (x, y) -> x.equals(y);
        System.out.println(predicate.test("123", "123"));

        BiPredicate<String, String> predicate1 = String::equals;
        System.out.println(predicate1.test("123", "123"));
    }

    //二、构造器引用
    @Test
    public void test4() {
        Supplier<String> supplier = () -> new String();
        System.out.println(supplier.get());

        Supplier<String> supplier1 = String::new;
        System.out.println(supplier1.get());

        // 构造器的参数列表要与函数接口对应方法参数列表对应
        Function<String, String> function = (x) -> new String(x);
        System.out.println(function.apply("123"));

        Function<String, String> function1 = String::new;
        System.out.println(function1.apply("123"));
    }

    //三、数组引用
    @Test
    public void test5() {
        Function<Integer, String[]> function = (x) -> new String[x];
        String[] apply = function.apply(10);
        System.out.println(apply.length);

        Function<Integer, String[]> function1 = String[]::new;
        String[] apply1 = function1.apply(20);
        System.out.println(apply1.length);

    }
}
