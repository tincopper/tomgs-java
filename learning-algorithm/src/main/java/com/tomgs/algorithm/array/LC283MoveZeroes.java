package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * LC283MoveZeroes
 * <p>
 * <a href="https://leetcode.cn/problems/move-zeroes/description/">283. 移动零</a>
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 * <p>
 * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC283MoveZeroes {

    public void moveZeroes(int[] nums) {
        // 定义两个指针，slow 用于指向0元素的指针，fast 指向非0元素的指针
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                // 有可能第一位不为0，所以这里需要使用中间的变量tmp，而不是直接赋值nums[fast] = 0; 这样不严谨
                int tmp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = tmp;
                slow++;
            }
            fast++;
        }
    }

    @Test
    public void test() {
        int[] nums = {0, 1, 0, 3, 12};

        moveZeroes(nums);

        System.out.println(Arrays.toString(nums));
    }

}
