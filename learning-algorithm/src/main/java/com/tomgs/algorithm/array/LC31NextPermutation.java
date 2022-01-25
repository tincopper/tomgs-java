package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * 31. 下一个排列
 * <p>
 * 实现获取 下一个排列 的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列（即，组合出下一个更大的整数）。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须 原地 修改，只允许使用额外常数空间。
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [1,2,3]
 * 输出：[1,3,2]
 * 示例 2：
 * <p>
 * 输入：nums = [3,2,1]
 * 输出：[1,2,3]
 * 示例 3：
 * <p>
 * 输入：nums = [1,1,5]
 * 输出：[1,5,1]
 * 示例 4：
 * <p>
 * 输入：nums = [1]
 * 输出：[1]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/next-permutation
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/25 1.0
 */
public class LC31NextPermutation {

    public void nextPermutation(int[] nums) {

    }

    @Test
    public void test() {
        int[] arg = {1, 2, 3};
        nextPermutation(arg);
        System.out.println(Arrays.toString(arg));
    }

    @Test
    public void test1() {
        int[] arg = {3, 2, 1};
        nextPermutation(arg);
        System.out.println(Arrays.toString(arg));
    }

    @Test
    public void test2() {
        int[] arg = {1, 1, 5};
        nextPermutation(arg);
        System.out.println(Arrays.toString(arg));
    }

    @Test
    public void test3() {
        int[] arg = {1};
        nextPermutation(arg);
        System.out.println(Arrays.toString(arg));
    }

}
