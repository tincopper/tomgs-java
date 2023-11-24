package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * LC03LengthOfLongestSubstring
 * <p>
 * <a href="https://leetcode.cn/problems/longest-substring-without-repeating-characters/">3. 无重复字符的最长子串</a>
 * <p>
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * 示例 1:
 * <p>
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 * <p>
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC03LengthOfLongestSubstring {

    public int lengthOfLongestSubstring(String s) {
        int maxLength = 0;
        if (s.isEmpty()) {
            return maxLength;
        }

        // 在java里面判断重复可以用hash表
        // 如果有重复将窗口的左边删除继续往右移
        Set<Character> set = new HashSet<>();
        int right = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0) {
                set.remove(s.charAt(i - 1));
            }
            while (right < s.length() && !set.contains(s.charAt(right))) {
                set.add(s.charAt(right));
                right++;
            }
            maxLength = Math.max(maxLength, right - i);
        }

        return maxLength;
    }

    @Test
    public void test() {
        String s = "abcabcbb";
        int result = lengthOfLongestSubstring(s);
        System.out.println(result);
    }
}
