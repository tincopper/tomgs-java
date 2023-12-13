package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.HashMap;
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

    public int lengthOfLongestSubstring2(String s) {
        // 判断是否存在重复的字符
        HashMap<Character, Integer> map = new HashMap<>();

        int left = 0, right = 0;
        int len = 0;
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;

            map.put(c, map.getOrDefault(c, 0) + 1);

            // 缩减窗口，有重复字符时需要缩减窗口
            while (map.get(c) > 1) {
                // 缩减窗口
                char d = s.charAt(left);
                left++;

                if (map.containsKey(d)) {
                    map.put(d, map.get(d) - 1);
                }
            }
            // 因为窗口收缩的 while 条件是存在重复元素，换句话说收缩完成后一定保证窗口中没有重复嘛。
            // 更新长度
            len = Math.max(len, right - left);
        }

        return len;
    }

    @Test
    public void test() {
        String s = "abcabcbb";
        int result = lengthOfLongestSubstring(s); // 3
        System.out.println(result);
    }

    @Test
    public void test2() {
        String s = "abcabcbb";
        int result = lengthOfLongestSubstring2(s); // 3
        System.out.println(result);
    }

    @Test
    public void test3() {
        String s = "pwwkew";
        int result = lengthOfLongestSubstring2(s); // 3
        System.out.println(result);
    }

    @Test
    public void test4() {
        String s = "bbbbb";
        int result = lengthOfLongestSubstring2(s); // 1
        System.out.println(result);
    }

    @Test
    public void test5() {
        String s = " ";
        int result = lengthOfLongestSubstring2(s); // 1
        System.out.println(result);
    }

    @Test
    public void test6() {
        String s = "";
        int result = lengthOfLongestSubstring2(s); // 0
        System.out.println(result);
    }
}
