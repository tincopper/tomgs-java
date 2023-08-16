package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * LC27RemoveElement
 * <p>
 * <a href="https://leetcode.cn/problems/remove-element/">27. 移除元素</a>
 * <p>
 *   给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC27RemoveElement {

    public int removeElement(int[] nums, int val) {
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }

        return slow;
    }

    @Test
    public void test() {
        int[] nums = {3, 2, 2, 3};
        int val = 3;

        final int len = removeElement(nums, val);

        for (int i = 0; i < len; i++) {
            System.out.println(nums[i]);
        }
    }
}
