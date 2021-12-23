package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * HardString
 *
 * @author tomgs
 * @since 2021/12/23
 */
public class HardString {

    @Test
    public void func1() {
        // 0, maxLen -> 1, maxLen
        String str = "banana";
        HardString hardString = new HardString();
        String result = hardString.longestDupSubstring(str);
        System.out.println(result);

        int i = 5 >> 1;
        System.out.println(i);
    }

    int[] nums;
    String res = "";
    long mod = (long) (1e12 + 7);

    public String longestDupSubstring2(String s) {
        Set<String> exists = new HashSet<>();
        int length = s.length();
        int maxLen = length >> 1;
        for (int i = 0; i < length; i++) {

        }
        return "";
    }

    /**
     * from <href>https://leetcode-cn.com/problems/longest-duplicate-substring/solution/java-hua-dong-chuang-kou-by-zhou-the-w2h4/</href>
     */
    public String longestDupSubstring(String s) {
        int n = s.length();
        nums = new int[n];
        int i = 0;
        for (char c : s.toCharArray()) {
            nums[i++] = c - 'a';
        }
        int left = 1, right = n;
        while (left < right) {
            int mid = (left + right) >> 1;
            if (check(s, mid)) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return res;
    }

    boolean check(String s, int len) {
        Set<Long> set = new HashSet<>();
        long cur = 0;
        for (int i = 0; i < len; i++) {
            cur = (cur * 26) % mod;
            cur = (cur + nums[i]) % mod;
        }
        set.add(cur);
        long full = 1;
        for (int i = 0; i < len; i++) {
            full = (full * 26) % mod;
        }
        int start = 0;
        int end = len;
        while (end < s.length()) {
            cur = ((cur * 26) % mod - (nums[start] * full) % mod + mod) % mod;
            cur = (cur + nums[end]) % mod;
            start++;
            end++;
            if (!set.add(cur)) {
                res = s.substring(start, end);
                return true;
            }
        }
        return false;
    }

}
