package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * <a href="https://leetcode.cn/problems/kth-largest-element-in-an-array/">215. 数组中的第K个最大元素</a>
 * <p>
 * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * <p>
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * <p>
 * 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
 *
 * @author tomgs
 * @version 1.0
 */
public class LC215FindKthLargest {

    public int findKthLargest(int[] nums, int k) {
        int res = 0;

        return res;
    }

    @Test
    public void test() {
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;

        int kthLargest = findKthLargest(nums, k);
        System.out.println(kthLargest);
    }

}
