package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * 383. 赎金信
 *
 * 给你两个字符串：ransomNote 和 magazine ，判断 ransomNote 能不能由 magazine 里面的字符构成。
 * 如果可以，返回 true ；否则返回 false 。
 * magazine 中的每个字符只能在 ransomNote 中使用一次。
 *
 * 示例 1：
 *
 * 输入：ransomNote = "a", magazine = "b"
 * 输出：false
 * 示例 2：
 *
 * 输入：ransomNote = "aa", magazine = "ab"
 * 输出：false
 * 示例 3：
 *
 * 输入：ransomNote = "aa", magazine = "aab"
 * 输出：true
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/ransom-note
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/20 1.0
 */
public class LC383CanConstruct {

    public boolean canConstruct(String ransomNote, String magazine) {
        return false;
    }

    @Test
    public void test() {
        String ransomNote = "a";
        String magazine = "b";
        boolean result = canConstruct(ransomNote, magazine);
        System.out.println(result);
    }

    @Test
    public void test1() {
        String ransomNote = "aa";
        String magazine = "ab";
        boolean result = canConstruct(ransomNote, magazine);
        System.out.println(result);
    }

    @Test
    public void test2() {
        String ransomNote = "aa";
        String magazine = "aab";
        boolean result = canConstruct(ransomNote, magazine);
        System.out.println(result);
    }

}
