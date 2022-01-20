package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 387. 字符串中的第一个唯一字符
 * <p>
 * 给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
 * <p>
 * 示例：
 * <p>
 * s = "leetcode"
 * 返回 0 : 表示第0个是唯一不重复的字符
 * <p>
 * s = "loveleetcode"
 * 返回 2 ：表示第2个是唯一不重复的字符
 * <p>
 * 提示：你可以假定该字符串只包含小写字母。
 *
 * @author tomgs
 * @version 2022/1/20 1.0
 */
public class LC387FirstUniqChar {

    /**
     * hash方法1
     * 遍历字符串方法：charAt、toCharArray
     * s.charAt(i) -> 时间复杂度为 O(1)，空间复杂度为O(1)，但是会有下标检测
     * toCharArray -> 时间复杂度为 O(n)，空间复杂度为O(n)，但是在生成数组之后的遍历是基于数组所以比较快
     */
    public int firstUniqChar(String s) {
        char[] chars = s.toCharArray(); // toCharArray需要额外的空间
        Map<Character, Integer> map = new HashMap<>();
        for (char aChar : chars) {
            map.put(aChar, map.getOrDefault(aChar, 0) + 1);
        }
        for (int i = 0; i < chars.length; i++) {
            if (map.get(chars[i]) == 1) {
                return i;
            }
        }
        return -1;
    }

    // hash方法2
    public int firstUniqChar2(String s) {
        // s.charAt(i) -> O(1)
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
        }
        for (int i = 0; i < s.length(); i++) {
            if (map.get(s.charAt(i)) == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 很秀的方法
     * 因为字符串只有26个字母，所以建立一个数组用于存放26个字母，然后建立映射关系。
      */
    public int firstUniqChar3(String s) {
        int[] arr = new int[26];
        int n = s.length();
        for (int i = 0; i < n; i++) {
            arr[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            if (arr[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void test() {
        String s = "leetcode";
        int result = firstUniqChar(s);
        System.out.println(result);
    }

    @Test
    public void test1() {
        String s = "loveleetcode";
        int result = firstUniqChar(s);
        System.out.println(result);
    }

    @Test
    public void test2() {
        String s = "leetcode";
        int result = firstUniqChar2(s);
        System.out.println(result);
    }

    @Test
    public void test3() {
        String s = "loveleetcode";
        int result = firstUniqChar2(s);
        System.out.println(result);
    }

    @Test
    public void test4() {
        String s = "leetcode";
        int result = firstUniqChar2(s);
        System.out.println(result);
    }

    @Test
    public void test5() {
        String s = "loveleetcode";
        int result = firstUniqChar2(s);
        System.out.println(result);
    }

}
