package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * LC541ReverseString
 * <p>
 * <a href="https://leetcode.cn/problems/reverse-string-ii/description/">541. 反转字符串 II</a>
 * <p>
 * 给定一个字符串 s 和一个整数 k，从字符串开头算起，每计数至 2k 个字符，就反转这 2k 字符中的前 k 个字符。
 * <p>
 * 如果剩余字符少于 k 个，则将剩余字符全部反转。
 * 如果剩余字符小于 2k 但大于或等于 k 个，则反转前 k 个字符，其余字符保持原样。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC541ReverseString2 {

    public String reverseStr(String s, int k) {
        char[] charArray = s.toCharArray();
        int len = s.length();
        for (int i = 0; i < len; i += 2 * k) {
            if (len - i < k) {
                reverse(charArray, i, len - 1);
            } else if (len - i < 2 * k && len - i >= k) {
                reverse(charArray, i, i + k - 1);
            } else {
                reverse(charArray, i, i + k - 1);
            }
        }
        return new String(charArray);
    }

    // 简化版
    public String reverseStr2(String s, int k) {
        char[] charArray = s.toCharArray();
        int len = s.length();
        for (int i = 0; i < len; i += 2 * k) {
            reverse(charArray, i, Math.min(i + k, len) - 1);
        }
        return new String(charArray);
    }

    private void reverse(char[] s, int l, int r) {
        while (l < r) {
            char tmp = s[l];
            s[l] = s[r];
            s[r] = tmp;
            l++;
            r--;
        }
    }

    @Test
    public void test() {
        String s = "abcdefg";
        int k = 2;

        final String result = reverseStr(s, k);
        System.out.println(result);
    }

}
