package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LC438FindAllAnagramsInString
 * <p>
 * <a href="https://leetcode.cn/problems/find-all-anagrams-in-a-string/">438. 找到字符串中所有字母异位词</a>
 * <p>
 * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
 *
 * 异位词 指由相同字母重排列形成的字符串（包括相同的字符串）。
 *
 * 示例 1:
 *
 * 输入: s = "cbaebabacd", p = "abc"
 * 输出: [0,6]
 * 解释:
 * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
 * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC438FindAllAnagramsInString {

    /**
     * 这个和 LC567PermutationInString 排列一样的，就是求子串，通过滑动窗口解决
     */
    public List<Integer> findAnagrams(String s, String p) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();

        int left = 0, right = 0;
        int valid = 0;
        // 存储结果
        List<Integer> res = new ArrayList<>();

        for (char c : p.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        while (right < s.length()) {
            char c = s.charAt(right);
            right++;

            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c))) {
                    // 只有当 window[c] 和 need[c] 对应的出现次数一致时，才能满足条件，valid 才能 +1，否则会有重复的数据时会出错
                    valid++;
                }
            }

            while (valid == need.size()) {
                // 获取符合的窗口
                // 因为是排列所以长度得相等
                if (right - left == p.length()) {
                    // 符合条件
                    res.add(left);
                }

                // 缩减窗口
                char d = s.charAt(left);
                left++;

                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }

        }

        return res;
    }

    @Test
    public void test() {
        String s = "cbaebabacd", p = "abc";
        System.out.println(findAnagrams(s, p));
    }

    @Test
    public void test1() {
        String s = "abab", p = "ab";
        System.out.println(findAnagrams(s, p));
    }
}
