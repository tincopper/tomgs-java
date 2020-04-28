package com.tomgs.core.java8.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangzy
 * @since 1.0
 */
public class TestAvg {

  public static void main(String[] args) {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(3);
    list.add(6);
    list.add(11);
    list.add(7);

    double v = list.stream().mapToInt(Integer::intValue).average().orElse(0);
    System.out.println(v);
  }
}
