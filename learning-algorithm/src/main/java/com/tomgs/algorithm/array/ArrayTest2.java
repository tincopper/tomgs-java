package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 *  
 *
 * @author tomgs
 * @version 2021/7/5 1.0 
 */
public class ArrayTest2 {

    @Test
    public void test() {
        int[] nums = {1, 2, 4, 0}; // 3
        int[] nums1 = {3, 4, -1, 1}; // 2
        int[] nums2 = {7, 8, 9, 11, 12}; // 1
        int[] nums3 = {1, 2, 0}; // 3
        int[] nums4 = {1, 2, 0, 5, 8, 9, 10, 11}; // 3
        int[] nums5 = {1, 2, 0, 4, 3, 6}; // 5
        int[] nums6 = {1, 4, 0}; // 2

        call(nums);
        call(nums1);
        call(nums2);
        call(nums3);
        call(nums4);
        call(nums5);
        call(nums6);
    }

    @Test
    public void test2() {
        int[] nums5 = {1, 2, 0, 4, 3, 6}; // 5
        call(nums5);
    }

    private void call(int[] nums) {
        int min = 1;
        int secMin = 1;
        boolean flag = false;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0) {
                continue;
            }
            if (nums[i] == 1 && min == 1) {
                ++min;
                flag = true;
                continue;
            }
            if (nums[i] > min) {
                /*if (nums[i++] - nums[i] > 1) {

                }*/
                int tmp = nums[i] - 1; // 3
                if (tmp == min) { //说明连续
                    continue;
                }
                if (flag) {
                    min = secMin == min ? tmp : Math.min(tmp, min);
                } else {
                    secMin = tmp;
                }
                continue;
            }
            if (nums[i] < min) {
                ++min;
            }
            if (nums[i] == min) {
                secMin = min;
            }
        }
        if (flag) {
            System.out.println(min);
        } else {
            System.out.println(Math.min(min, secMin));
        }
    }

}
