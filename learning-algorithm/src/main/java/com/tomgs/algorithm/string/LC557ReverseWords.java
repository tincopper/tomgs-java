package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * LC557ReverseWords
 * <p>
 * <a href="https://leetcode.cn/problems/reverse-words-in-a-string-iii/description/">557. 反转字符串中的单词 III</a>
 * <p>
 * 给定一个字符串 s ，你需要反转字符串中每个单词的字符顺序，同时仍保留空格和单词的初始顺序。
 * 示例 1：
 * <p>
 * 输入：s = "Let's take LeetCode contest"
 * 输出："s'teL ekat edoCteeL tsetnoc"
 *
 * @author tomgs
 * @since 1.0
 */
public class LC557ReverseWords {

    public String reverseWords(String s) {
        s += " ";
        char[] charArray = s.toCharArray();
        int len = charArray.length;
        int l = 0;
        for (int i = 0; i < len; i++) {
            if (charArray[i] == ' ') {
                reverse(charArray, l, i - 1);
                l = i + 1;
            }
        }

        return new String(charArray, 0, len - 1);
    }

    public String reverseWords2(String s) {
        char[] charArray = s.toCharArray();
        int len = charArray.length;
        int l = 0;

        for (int i = 0; i < len; i++) {
            if (charArray[i] == ' ') {
                reverse(charArray, l, i - 1);
                l = i + 1;
            }
        }

        return new String(charArray, 0, len);
    }

    private void reverse(char[] s, int l, int r) {
        while (l < r) {
            char tmp = s[l];
            s[l] = s[r];
            s[r] = tmp;
            l++;
            r--;
        }
    }

    @Test
    public void test() {
        String s = "Let's take LeetCode contest";
        final String result = reverseWords(s);
        System.out.println(result);
    }

}
