package com.tomgs.disruptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tomgs
 * @since 2020/8/13
 */
public class AppMain {

  public static void main(String[] args) {
    Map<String, Boolean> map = new HashMap<>();
    map.put("a", null);
    map.put("b", null);
    map.put("c", false);

    System.out.println(map);

  }

}
