package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * LC05LongestPalindrome
 * <p>
 * <a href="https://leetcode.cn/problems/longest-palindromic-substring/">5. 最长回文子串</a>
 * <p>
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 * <p>
 * 如果字符串的反序与原始字符串相同，则该字符串称为回文字符串。
 * <p>
 * 回文串就是正着读和反着读都一样的字符串。
 * @author tomgs
 * @since 1.0
 */
public class LC05LongestPalindrome {

    public String longestPalindrome(String s) {
        String longest = "";
        for (int i = 0; i < s.length(); i++) {
            final String s1 = findPalindrome(s, i, i);
            final String s2 = findPalindrome(s, i, i + 1);
            longest = longest.length() > s1.length() ? longest : s1;
            longest = longest.length() > s2.length() ? longest : s2;
        }

        return longest;
    }

    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    private String findPalindrome(String s, int l, int r) {
        // 往两边展开
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        return s.substring(l + 1, r);
    }

    @Test
    public void test() {
        //String s = "babad";
        String s = "cbbd";
        final String result = longestPalindrome(s);
        System.out.println(result);
    }

    @Test
    public void test1() {
        String s = "bab";
        final boolean palindrome = isPalindrome(s);
        System.out.println(palindrome);
    }

    @Test
    public void test2() {
        String s = "bab";
        final String palindrome = findPalindrome(s, s.length() / 2, s.length() / 2);
        System.out.println(palindrome);
    }

}
