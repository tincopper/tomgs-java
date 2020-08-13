package com.tomgs.algorithm.list;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author tomgs
 * @since 2020/7/8
 */
public class Test {

  public static void main(String[] args) {
    int i = 10 % 2;
    int i1 = 1 % 2;
    int i2 = 3 % 2;
    System.out.println(i);
    System.out.println(i1);
    System.out.println(i2);

    ArrayList<String> copySet = new ArrayList<>();
    copySet.add("aaaa");
    copySet.add("bbbb");
    copySet.add("cccc");
    // copy set leader election.
    // servers.sort(Comparable::compareTo);
    for (int j = 0; j < copySet.size(); j++) {
      String leader = copySet.get(new Random().nextInt(copySet.size()));
      System.out.println(leader);
    }
  }

}
