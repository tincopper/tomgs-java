package com.tomgs.core.list;

import java.util.LinkedList;

/**
 * @author tomgs
 * @since 2021/1/20
 */
public class TestLinkedList {

  public static void main(String[] args) {
    LinkedList<String> linkedList = new LinkedList<>();
    long start = System.currentTimeMillis();

    for (int i = 0; i < 10000000; i++) {
      linkedList.add("" + i);
    }

    for (int i = 0; i < 10000000; i++) {
      linkedList.remove();
    }

    long end = System.currentTimeMillis();

    System.out.println((double) (end - start) / 1000.0);
  }

}
