package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * 字符串相关算法
 *
 * @author tomgs
 * @since 2020/11/16
 */
public class BaseString {

  /*
    给定一个字符串，要求把字符串前面的若干个字符移动到字符串的尾部，如把字符串“abcdef”前面的2个字符’a’和’b’移动到字符串的尾部，
    使得原字符串变成字符串“cdefab”。请写一个函数完成此功能，要求对长度为n的字符串操作的时间复杂度为 O(n)，空间复杂度为 O(1)。

    https://www.bookstack.cn/read/The-Art-Of-Programming-By-July/ebook-zh-01.01.md
   */
  @Test
  public void test1() {
    // 方式一：暴力解法
    String str = "abcdef";
    char[] chars = str.toCharArray();
    ReversString.leftRotateString(chars, 6, 2);
    System.out.println(chars);
    // 方式二：三步反转法（手摇法）


  }

}

class ReversString {

  /**
   * 将第一个与后面一个进行反转
   * @param strs
   * @param n
   */
  public static void leftRotateString(char[] strs, int n) {
    char t = strs[0];
    // 这里不能直接进行交换
    for (int i = 1; i < n; i++) {
      strs[i - 1] = strs[i];
    }
    strs[n - 1] = t;
  }

  public static void leftRotateString(char[] strs, int n, int m) {
    for (int i = 0; i < m; i++) {
      leftRotateString(strs, n);
    }
  }

}