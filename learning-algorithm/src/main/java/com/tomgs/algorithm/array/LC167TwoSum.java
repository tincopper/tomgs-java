package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * LC167TwoSum
 * <p>
 * <a href="https://leetcode.cn/problems/two-sum-ii-input-array-is-sorted/">167. 两数之和 II - 输入有序数组</a>
 * <p>
 * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按 非递减顺序排列  ，请你从数组中找出满足相加之和等于目标数 target 的两个数。
 * 如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，则 1 <= index1 < index2 <= numbers.length 。
 * <p>
 * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
 * <p>
 * 你可以假设每个输入 只对应唯一的答案 ，而且你 不可以 重复使用相同的元素。
 * 你所设计的解决方案必须只使用常量级的额外空间。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC167TwoSum {

    public int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            final int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[] {left + 1, right + 1};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return new int[] {-1, -1};
    }

    @Test
    public void test() {
        int[] nums = {2, 5, 7, 11, 15};
        int target = 9;

        final int[] result = twoSum(nums, target);
        System.out.println(Arrays.toString(result));
    }

    @Test
    public void test2() {
        int[] nums = {3, 2, 4};
        int target = 6;

        final int[] result = twoSum(nums, target);
        System.out.println(Arrays.toString(result));
    }

}
