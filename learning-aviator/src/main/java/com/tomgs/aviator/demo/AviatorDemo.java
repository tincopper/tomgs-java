package com.tomgs.aviator.demo;

import com.googlecode.aviator.AviatorEvaluator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/4/23
 */
public class AviatorDemo {

  @Test
  public void testSimple() {
    //结果是 Long，而不是 Integer。这是因为 Aviator 的数值类型仅支持Long 和 Double，任何整数都将转换成 Long，任何浮点数都将转换为 Double，包括用户传入的变量数值。
    Long result = (Long) AviatorEvaluator.execute("1+2+3");
    System.out.println(result);
  }

  @Test
  public void test2() {
    Map<String, Object> env = new HashMap<>();
    env.put("name", "tomgs");
    String result = (String) AviatorEvaluator.execute(" 'hello ' + name", env);
    System.out.println(result);
  }

  @Test
  public void test3() {
    Map<String, Object> env = new HashMap<>();
    env.put("cpu", 89.99);
    env.put("mem", 80);
    boolean result = (boolean) AviatorEvaluator.execute("cpu >= 80 && mem >= 80", env);
    System.out.println(result);
  }

  @Test
  public void test4() {
    final List<String> list = new ArrayList<>();
    list.add("hello");
    list.add(" world");

    final int[] array = new int[3];
    array[0] = 0;
    array[1] = 1;
    array[2] = 3;

    final Map<String, Date> map = new HashMap<>();
    map.put("date", new Date());
    Map<String, Object> env = new HashMap<>();
    env.put("list", list);
    env.put("array", array);
    env.put("mmap", map);

    System.out.println(AviatorEvaluator.execute(
        "list[0] + list[1] + '\narray[0] + array[1] + array[2] = ' + (array[0] + array[1] + array[2]) + '\ntoday is ' + mmap.date", env));

    // int [][] a=......
    //AviatorEvaluator.exec("a[0][1]+a[0][0]",a);
  }

}
