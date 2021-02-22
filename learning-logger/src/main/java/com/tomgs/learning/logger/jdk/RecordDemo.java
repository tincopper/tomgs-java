package com.tomgs.learning.logger.jdk;

/**
 * @author tomgs
 * @since 2021/2/22
 */
public class RecordDemo {

  public static void main(String[] args) {
    RecordLog.info("123");
    RecordLog.info("456");
    RecordLog.info("abc{0}", 123);
    RecordLog.info("a{0}b{1}c{2}", 1, 2, 3);
  }

}
