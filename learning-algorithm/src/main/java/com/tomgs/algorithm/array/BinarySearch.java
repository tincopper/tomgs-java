package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * BinarySearch
 *
 * @author tomgs
 * @since 1.0
 */
public class BinarySearch {

    int binarySearch(int[] nums, int target) {
        // 一左一右两个指针相向而行
        int left = 0, right = nums.length - 1;
        while(left <= right) {
            int mid = (right + left) / 2;
            if(nums[mid] == target)
                return mid;
            else if (nums[mid] < target)
                left = mid + 1;
            else if (nums[mid] > target)
                right = mid - 1;
        }
        return -1;
    }

    @Test
    public void test() {
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        final int result = binarySearch(nums, 2);
        System.out.println(result);
    }

}
