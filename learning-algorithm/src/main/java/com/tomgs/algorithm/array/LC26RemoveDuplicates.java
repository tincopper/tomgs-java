package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * LC26RemoveDuplicates
 * <p>
 * 26. 删除有序数组中的重复项
 * <p>
 * 给你一个 升序排列 的数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
 * 元素的 相对顺序 应该保持 一致 。然后返回 nums 中唯一元素的个数。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC26RemoveDuplicates {

    public int removeDuplicates(int[] nums) {
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[slow] != nums[fast]) {
                slow++;
                nums[slow] = nums[fast];
            }
            fast++;
        }

        return slow + 1;
    }

    @Test
    public void test1() {
        //int[] nums = {1, 1, 2};
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        final int slow = removeDuplicates(nums);
        for (int i = 0; i < slow; i++) {
            System.out.println(nums[i]);
        }
    }

}
