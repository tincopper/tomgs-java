package com.tomgs.algorithm.string;

import org.junit.Test;

/**
 * LC151ReverseWords
 * <p>
 * <a href="https://leetcode.cn/problems/reverse-words-in-a-string/description/">151. 反转字符串中的单词</a>
 * <p>
 * 给你一个字符串 s ，请你反转字符串中 单词 的顺序。
 * 单词 是由非空格字符组成的字符串。s 中使用至少一个空格将字符串中的 单词 分隔开。
 * 返回 单词 顺序颠倒且 单词 之间用单个空格连接的结果字符串。
 * <p>
 * 注意：输入字符串 s中可能会存在前导空格、尾随空格或者单词间的多个空格。返回的结果字符串中，单词间应当仅用单个空格分隔，且不包含任何额外的空格。
 * <p>
 * 示例 1：
 * <p>
 * 输入：s = "the sky is blue"
 * 输出："blue is sky the"
 *
 * @author tomgs
 * @since 1.0
 */
public class LC151ReverseWords {

    /**
     * 1、常规做法是把 s 按空格 split 成若干单词，然后 reverse 这些单词的顺序，最后把这些单词 join 成句子。但这种方式使用了额外的空间，并不是「原地反转」单词。
     * 2、原地反转所有单词的顺序
     *   2.1 可以先将整个字符串反转
     *   2.2 再将每个单词反转
     */
    public String reverseWords(String s) {
        char[] chars = s.trim().toCharArray();
        // 反转字符串
        int left = 0, right = chars.length - 1;
        reverseString(chars, left, right);
        // 反转单词
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                // 如果有多余空格，将多余空格移动到词的最后面
                if (i + 1 <= right && chars[i + 1] == ' ') {
                    moveSpaceChar(chars, i + 1, right);
                }

                reverseString(chars, left, i - 1);
                left = i + 1;
            }
            if (i == chars.length - 1) {
                reverseString(chars, left, i);
            }
        }

        return new String(chars);
    }

    public String reverseWords2(String s) {
        StringBuilder sb = trimSpaces(s);

        // 翻转字符串
        reverse(sb, 0, sb.length() - 1);
        // 翻转每个单词
        reverseEachWord(sb);
        return sb.toString();
    }

    public StringBuilder trimSpaces(String s) {
        int left = 0, right = s.length() - 1;
        // 去掉字符串开头的空白字符
        while (left <= right && s.charAt(left) == ' ') {
            ++left;
        }

        // 去掉字符串末尾的空白字符
        while (left <= right && s.charAt(right) == ' ') {
            --right;
        }

        // 将字符串间多余的空白字符去除
        StringBuilder sb = new StringBuilder();
        while (left <= right) {
            char c = s.charAt(left);

            if (c != ' ') {
                sb.append(c);
            } else if (sb.charAt(sb.length() - 1) != ' ') {
                sb.append(c);
            }

            ++left;
        }
        return sb;
    }

    public void reverse(StringBuilder sb, int left, int right) {
        while (left < right) {
            char tmp = sb.charAt(left);
            sb.setCharAt(left++, sb.charAt(right));
            sb.setCharAt(right--, tmp);
        }
    }

    public void reverseEachWord(StringBuilder sb) {
        int n = sb.length();
        int start = 0, end = 0;

        while (start < n) {
            // 循环至单词的末尾
            while (end < n && sb.charAt(end) != ' ') {
                ++end;
            }
            // 翻转单词
            reverse(sb, start, end - 1);
            // 更新start，去找下一个单词
            start = end + 1;
            ++end;
        }
    }

    private static void moveSpaceChar(char[] chars, int left, int right) {
        // 定义两个指针，slow 用于指向0元素的指针，fast 指向非0元素的指针
        int slow = left, fast = left;
        while (fast <= right) {
            if (chars[fast] != ' ') {
                // 有可能第一位不为0，所以这里需要使用中间的变量tmp，而不是直接赋值nums[fast] = 0; 这样不严谨
                char tmp = chars[slow];
                chars[slow] = chars[fast];
                chars[fast] = tmp;
                slow++;
            }
            fast++;
        }
    }

    private static void reverseString(char[] chars, int left, int right) {
        while (left < right) {
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
            left++;
            right--;
        }
    }

    @Test
    public void test() {
        String s = "the sky is blue";
        System.out.println(reverseWords2(s));
    }

    @Test
    public void test1() {
        String s = "a good   example";
        System.out.println(reverseWords2(s));
    }

    @Test
    public void test2() {
        String s = "  hello world  ";
        System.out.println(reverseWords2(s));
    }

    @Test
    public void test3() {
        String s = "  Bob    Loves  Alice   ";
        System.out.println(reverseWords2(s));
    }
}
