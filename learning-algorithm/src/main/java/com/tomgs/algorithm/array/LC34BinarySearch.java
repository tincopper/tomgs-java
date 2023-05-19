package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * LC34BinarySearch
 * <p>
 * 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。
 * <p>
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * <p>
 * 你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [5,7,7,8,8,10], target = 8
 * 输出：[3,4]
 * 示例 2：
 * <p>
 * 输入：nums = [5,7,7,8,8,10], target = 6
 * 输出：[-1,-1]
 * 示例 3：
 * <p>
 * 输入：nums = [], target = 0
 * 输出：[-1,-1]
 *
 * @author tomgs
 * @since 1.0
 */
public class LC34BinarySearch {

    public int[] searchRange(int[] nums, int target) {
        int[] result = {-1, -1};
        if (nums == null || nums.length == 0) {
            return result;
        }

        int l = search(nums, target);
        int r = search(nums, target + 1);

        return l == r ? new int[]{-1, -1} : new int[] {l, r -1};
    }

    private int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) >> 1;
            if (nums[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    @Test
    public void test() {
        int[] nums = {5, 7, 7, 8, 8, 10};
        int target = 7;

        final int[] result = searchRange(nums, target);
        System.out.println(Arrays.toString(result));
    }

}
