package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * LC76MinWindowSubstring
 * <a href="https://leetcode.cn/problems/minimum-window-substring/description/">76. 最小覆盖子串</a>
 * <p>
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 * 注意：
 *
 * 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
 * 如果 s 中存在这样的子串，我们保证它是唯一的答案。
 *
 * 示例 1：
 *
 * 输入：s = "ADOBECODEBANC", t = "ABC"
 * 输出："BANC"
 * 解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
 * 示例 2：
 *
 * 输入：s = "a", t = "a"
 * 输出："a"
 * 解释：整个字符串 s 是最小覆盖子串。
 * 示例 3:
 *
 * 输入: s = "a", t = "aa"
 * 输出: ""
 * 解释: t 中两个字符 'a' 均应包含在 s 的子串中，
 * 因此没有符合条件的子字符串，返回空字符串。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC76MinWindowSubstring {

    public String minWindow(String s, String t) {
        // 定义窗口
        int left = 0, right = 0;
        //  定义窗口内需要的字符
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();

        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int valid = 0;
        int start = 0;
        int len = Integer.MAX_VALUE;
        final char[] chars = s.toCharArray();
        while (right < chars.length) {
            // 扩大窗口
            char c = chars[right];
            right++;
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                // 判断是否满足子串
                if (window.get(c).equals(need.get(c))) {
                    // 找到一个合法的子串
                    valid++;
                }
            }

            // 找到符合的子串了，缩减窗口
            while (valid == need.size()) {
                // 更新最小覆盖子串
                if (right - left < len) {
                    start = left;
                    len = right - left;
                }
                // 移出左窗口数据
                char c1 = chars[left];
                left++;
                if (need.containsKey(c1)) {
                    if (window.get(c1).equals(need.get(c1))) {
                        valid--;
                    }
                    window.put(c1, window.get(c1) - 1);
                }

            }

        }

        return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
    }

    @Test
    public void test() {
        String s = "ADOBECODEBANC", t = "ABC";
        System.out.println(minWindow(s, t));
    }

    @Test
    public void test2() {
        String s = "ABAACBAB", t = "ABC";
        System.out.println(minWindow(s, t));
    }

}
