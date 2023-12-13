package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.HashMap;

/**
 * LC567PermutationInString
 * <p>
 * <a href="https://leetcode.cn/problems/permutation-in-string/">567. 字符串的排列</a>
 * <p>
 * 给你两个字符串 s1 和 s2 ，写一个函数来判断 s2 是否包含 s1 的排列。如果是，返回 true ；否则，返回 false 。
 * 换句话说，s1 的排列之一是 s2 的 子串 。
 *
 * 示例 1：
 *
 * 输入：s1 = "ab" s2 = "eidbaooo"
 * 输出：true
 * 解释：s2 包含 s1 的排列之一 ("ba").
 * 示例 2：
 *
 * 输入：s1= "ab" s2 = "eidboaoo"
 * 输出：false
 *
 * @author tomgs
 * @since 1.0
 */
public class LC567PermutationInString {

    public boolean checkInclusion(String s1, String s2) {
        HashMap<Character, Integer> need = new HashMap<>();
        HashMap<Character, Integer> window = new HashMap<>();

        // 1、定义窗口
        int left = 0, right = 0;
        for (char c : s1.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int valid = 0;
        while (right < s2.length()) {
            // 2、扩展窗口
            char c =  s2.charAt(right);
            right++;
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c))) {
                    valid++;
                }
            }

            // 3、找到符合要求的窗口
            // 合法的长度应该 == s1的长度
            while (valid == need.size()) {
                // 4、更新窗口大小（重点）
                // 判断是否满足题目要求的条件（如最小子串、满足排列的子串等等）
                if (right - left == s1.length()) {
                    return true;
                }

                // 5、不满足条件则缩减窗口
                char d = s2.charAt(left);
                left++;

                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }

        return false;
    }

    @Test
    public void test() {
        String s1 = "ab", s2 = "eidbaooo";
        System.out.println(checkInclusion(s1, s2)); // true
    }

    @Test
    public void test1() {
        String s1 = "ab", s2 = "eidboaooo";
        System.out.println(checkInclusion(s1, s2)); // false
    }
}
