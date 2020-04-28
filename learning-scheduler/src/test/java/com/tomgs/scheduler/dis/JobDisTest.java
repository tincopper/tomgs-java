package com.tomgs.scheduler.dis;

import java.util.UUID;
import org.junit.Test;

/**
 * @author tangzy
 * @since 1.0
 */
public class JobDisTest {

  static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }

  @Test
  public void test01() {
    int n = 3;
    int count = 0;
    for (;;) {
      count++;
      int index = (n - 1) & hash(UUID.randomUUID().toString());
      System.out.println("index: " + index);
      if (index == 1) {
        System.out.println("count: " + count);
        break;
      }
    }
  }

}
