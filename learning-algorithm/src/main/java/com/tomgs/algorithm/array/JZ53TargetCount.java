package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * <a href="https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/description/">剑指 Offer 53 - I. 在排序数组中查找数字 I</a>
 * <p>
 * 统计一个数字在排序数组中出现的次数。
 * <p>
 * 输入: nums = [5,7,7,8,8,10], target = 8
 * 输出: 2
 *
 * @author tomgs
 * @version 1.0
 */
public class JZ53TargetCount {

    public int search(int[] nums, int target) {
        int leftIdx = binarySearch(nums, target, true);
        int rightIdx = binarySearch(nums, target, false) - 1;
        if (leftIdx <= rightIdx && rightIdx < nums.length && nums[leftIdx] == target && nums[rightIdx] == target) {
            return rightIdx - leftIdx + 1;
        }
        return 0;
    }

    public int binarySearch(int[] nums, int target, boolean lower) {
        int left = 0, right = nums.length - 1, ans = nums.length;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] > target || (lower && nums[mid] >= target)) {
                right = mid - 1;
                ans = mid;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    @Test
    public void test() {
        int[] nums = {2, 2};
        int count = search(nums, 2);
        System.out.println(count);

        int[] nums1 = {5, 7, 7, 8, 8, 10};
        int count1 = search(nums1, 8);
        System.out.println(count1);
    }

}
