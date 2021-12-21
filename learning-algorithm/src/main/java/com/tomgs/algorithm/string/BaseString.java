package com.tomgs.algorithm.string;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 字符串相关算法
 *
 * @author tomgs
 * @since 2020/11/16
 */
public class BaseString {

    /*
      给定一个字符串，要求把字符串前面的若干个字符移动到字符串的尾部，如把字符串“abcdef”前面的2个字符’a’和’b’移动到字符串的尾部，
      使得原字符串变成字符串“cdefab”。请写一个函数完成此功能，要求对长度为n的字符串操作的时间复杂度为 O(n)，空间复杂度为 O(1)。

      https://www.bookstack.cn/read/The-Art-Of-Programming-By-July/ebook-zh-01.01.md
     */
    @Test
    public void test1() {
        // 方式一：暴力解法 时间复杂度O(n * m) 不符合要求
        String str = "abcdef";
        char[] chars = str.toCharArray();
        ReversString.leftRotateString(chars, 6, 2);
        System.out.println(chars);
        // 方式二：三步反转法（手摇法）
    /*
     * 将一个字符串分成X和Y两个部分，在每部分字符串上定义反转操作，如X^T，即把X的所有字符反转（如，X=”abc”，那么X^T=”cba”），那么就得到下面的结论：(X^TY^T)^T=YX，显然就解决了字符串的反转问题。
     *
     * 例如，字符串 abcdef ，若要让def翻转到abc的前头，只要按照下述3个步骤操作即可：
        首先将原字符串分为两个部分，即X:abc，Y:def；
        将X反转，X->X^T，即得：abc->cba；将Y反转，Y->Y^T，即得：def->fed。
        反转上述步骤得到的结果字符串X^TY^T，即反转字符串cbafed的两部分（cba和fed）给予反转，cbafed得到defabc，形式化表示为(X^TY^T)^T=YX，这就实现了整个反转。
     */


    }

    /**
     * 异或运算练习
     * 性质
     * 1、交换律
     * 2、结合律（即(a^b)^c == a^(b^c)）
     * 3、对于任何数x，都有x^x=0，x^0=x
     * 4、自反性 A XOR B XOR B = A xor 0 = A
     * <p>
     * A=A XOR B (a XOR b)
     * B=B XOR A (b XOR a XOR b = a)
     * A=A XOR B (a XOR b XOR a = b)
     *
     * https://www.iteye.com/blog/nassir-1994914
     */
    @Test
    public void test2() {
        int a = 5, b = 10;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;

        System.out.println(a);
        System.out.println(b);
    }

    /**
     * https://blog.csdn.net/qq_34581118/article/details/78452316
     *
     * google: 一个数组存放若干整数，一个数出现奇数次，其余数均出现偶数次，找出这个出现奇数次的数？
     */
    @Test
    public void fun() {
        int[] a = {22, 38, 38, 22, 22, 4, 4, 11, 11};
        int temp = 0;
        for (int i = 0; i < a.length; i++) {
            temp ^= a[i];
        }
        System.out.println(temp);
    }

    /**
     * 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。你可以按 任意顺序 返回答案。
     */
    @Test
    public void fun1() {
        // T:O(n)  S: O(1)
        int[] nums = {22, 38, 22, 38, 4, 1, 4, 11, 11, 3};
        int temp = nums[0];
        int temp1 = nums[1];
        for (int i = 2; i < nums.length; i++) {
            int i1 = temp ^ nums[i];
            int i2 = temp1 ^ nums[i];

            if (i1 == 0) {
                temp = 0;
                continue;
            }
            if (i2 == 0) {
                temp1 = 0;
                continue;
            }
            if (temp == 0 && temp1 == 0) {
                temp = nums[i];
                continue;
            }
            if (temp == 0) {
                temp = nums[i];
            }
            if (temp1 == 0) {
                temp1 = nums[i];
            }
        }
        System.out.println(temp);
        System.out.println(temp1);
    }

    /**
     * 两个不相等的元素在位级表示上必定会有一位存在不同。
     *
     * 将数组的所有元素异或得到的结果为不存在重复的两个元素异或的结果。
     *
     * diff &= -diff 得到出 diff 最右侧不为 0 的位，也就是不存在重复的两个元素在位级表示上最右侧不同的那一位，利用这一位就可以将两个元素区分开来。
     *
     * ```java
     * public int[] singleNumber(int[] nums) {
     *         int diff = 0;
     *         for (int num : nums) diff ^= num;
     *         diff &= -diff;  // 得到最右一位
     *         int[] ret = new int[2];
     *         for (int num : nums) {
     *             if ((num & diff) == 0) ret[0] ^= num;
     *             else ret[1] ^= num;
     *         }
     *         return ret;
     *     }
     * ```
     */
    @Test
    public void func2() {
        int[] nums = {22, 38, 22, 38, 4, 4, 11, 11, 1, 3};
        int[] result = singleNumber(nums);
        for (int i : result) {
            System.out.println(i);
        }
    }

    public int[] singleNumber(int[] nums) {
        int diff = 0;
        for (int num : nums)
            diff ^= num;
        diff &= -diff;  // 得到最右一位
        int[] ret = new int[2];
        for (int num : nums) {
            if ((num & diff) == 0)
                ret[0] ^= num;
            else
                ret[1] ^= num;
        }
        return ret;
    }

    /**
     * 输入一个字符串，求这个字符串中存在最大不重复子串的长度，
     * 例如
     * 输入abcdabcbb，最大不重复子串abc，即输出 4;
     * 输入bbbbbbb，最大不重复子串b，即输出 1;
     * 输入pwwkew，最大不重复子串kew，即输出 3；
     *
     * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/solution/wu-zhong-fu-zi-fu-de-zui-chang-zi-chuan-by-leetc-2/
     */
    @Test
    public void func3() {
        String str = "pwwkew";
        int i = lengthOfLongestSubstring(str);
        System.out.println(i);
    }
    public int lengthOfLongestSubstring(String s) {
        // 哈希集合，记录每个字符是否出现过
        Set<Character> occ = new HashSet<Character>();
        int n = s.length();
        // 右指针，初始值为 -1，相当于我们在字符串的左边界的左侧，还没有开始移动
        int rk = -1, ans = 0;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                // 左指针向右移动一格，移除一个字符
                occ.remove(s.charAt(i - 1));
            }
            while (rk + 1 < n && !occ.contains(s.charAt(rk + 1))) {
                // 不断地移动右指针
                occ.add(s.charAt(rk + 1));
                ++rk;
            }
            // 第 i 到 rk 个字符是一个极长的无重复字符子串
            ans = Math.max(ans, rk - i + 1);
        }
        return ans;
    }
}

class ReversString {

    /**
     * 将第一个与后面一个进行反转
     *
     * @param strs
     * @param n
     */
    public static void leftRotateString(char[] strs, int n) {
        char t = strs[0];
        // 这里不能直接进行交换
        for (int i = 1; i < n; i++) {
            strs[i - 1] = strs[i];
        }
        strs[n - 1] = t;
    }

    public static void leftRotateString(char[] strs, int n, int m) {
        for (int i = 0; i < m; i++) {
            leftRotateString(strs, n);
        }
    }

}