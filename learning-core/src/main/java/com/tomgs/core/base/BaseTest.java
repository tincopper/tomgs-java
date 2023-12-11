package com.tomgs.core.base;

import org.junit.Test;

/**
 * @author tangzy
 * @since 1.0
 */
public class BaseTest {

  @Test
  public void test01() {
    int windowLengthInMs = 500;
    long timeMillis = System.currentTimeMillis();
    int[] array = new int[2];

    long timeId = timeMillis / windowLengthInMs;
    // Calculate current index so we can map the timestamp to the leap array.
    int length = array.length;
    int idx = (int) (timeId % length);
    System.out.println("array index:" + idx);

    long windowStart = timeMillis - timeMillis % windowLengthInMs;
    System.out.println("start time:" + windowStart);
  }

  @Test
  public void test02() {
    int[] arr = {1, 2, 3};
    int[] arr2 = {1, 2, 3, 4, 5};
    for (int i = 0; i < arr2.length; i++) {
      int index = i % arr.length;
      //System.out.println(arr[index]);
      System.out.println(index);
    }
  }
}
