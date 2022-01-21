package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.*;

/**
 * 242. 有效的字母异位词
 *
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 * 字母异位词指字母相同，但排列不同的字符串。
 *
 * 注意：若s 和 t中每个字符出现的次数都相同，则称s 和 t互为字母异位词。
 * 异位词等价于「两个字符串排序后相等」。
 *
 * 示例1:
 * 输入: s = "anagram", t = "nagaram"
 * 输出: true
 * 示例 2:
 *
 * 输入: s = "rat", t = "car"
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-anagram
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/21 1.0
 */
public class LC242IsAnagram {

    // 最简单的方法
    public boolean isAnagram3(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] str1 = s.toCharArray();
        char[] str2 = t.toCharArray();
        Arrays.sort(str1);
        Arrays.sort(str2);
        return Arrays.equals(str1, str2);
    }

    // hash法
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        // char -> count
        Map<Character, Integer> map = new HashMap<>();
        char[] m = s.toCharArray();
        for (char c : m) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        char[] n = t.toCharArray();
        for (char c : n) {
            Integer count = map.getOrDefault(c, 0);
            count--;
            if (count < 0) {
                return false;
            }
            map.put(c, count);
        }
        return true;
    }

    // 特殊法，和LC383方法一致：推荐
    public boolean isAnagram2(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] arr = new int[26];
        for (int i = 0; i < s.length(); i++) {
            arr[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < t.length(); i++) {
            int index = t.charAt(i) - 'a';
            arr[index]--;
            if (arr[index] < 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test() {
        String s = "anagram", t = "nagaram";
        boolean result = isAnagram(s, t);
        System.out.println(result);
    }

    @Test
    public void test1() {
        String s = "rat", t = "car";
        boolean result = isAnagram(s, t);
        System.out.println(result);
    }

    @Test
    public void test2() {
        String s = "acsrsfs", t = "carsssf";
        boolean result = isAnagram(s, t);
        System.out.println(result);
    }

    @Test
    public void test3() {
        String s = "ab", t = "a";
        boolean result = isAnagram(s, t);
        System.out.println(result);
    }

    @Test
    public void test4() {
        String s = "aacc", t = "ccac";
        boolean result = isAnagram(s, t);
        System.out.println(result);
    }

    @Test
    public void test5() {
        String s = "ab", t = "a";
        boolean result = isAnagram2(s, t);
        System.out.println(result);
    }

    @Test
    public void test6() {
        String s = "aacc", t = "ccac";
        boolean result = isAnagram2(s, t);
        System.out.println(result);
    }
}
