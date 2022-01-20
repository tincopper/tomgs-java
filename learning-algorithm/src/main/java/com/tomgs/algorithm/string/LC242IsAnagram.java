package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * 242. 有效的字母异位词
 *
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 *
 * 注意：若s 和 t中每个字符出现的次数都相同，则称s 和 t互为字母异位词。
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

    public boolean isAnagram(String s, String t) {
        return false;
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
}
