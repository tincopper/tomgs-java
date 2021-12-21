package com.tomgs.algorithm.array;

import java.util.Arrays;

/**
 *  合并有序数组：
 *  给定两个有序整数数组nums1和nums2，将nums2合并到nums1中，使得nums1成为一个有序的数组。
 *  <p>
 *      示例：输入 nums1 = [1, 2, 3, 0, 0, 0], m = 3;
 *                nums2 = [2, 5, 6], n = 3;
 *            输出: [1, 2, 2, 3, 5, 6]
 *  <p/>
 *  要求：时间复杂度O(n)，空间复杂度O(1)
 *
 * @author tomgs
 * @version 2019/11/18 1.0 
 */
public class MergeTwoArray {

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 0, 0, 0, 0};
        int[] nums2 = {1, 2, 5, 6};

        //method1(nums1, 3, nums2, 3);
//        method2(nums1, 3, nums2, 3);
        method3(nums1, 3, nums2, 4);

        System.out.println(Arrays.toString(nums1));
    }

    /**
     * 合并后排序：
     * 时间复杂度：O((n + m) / log(n + m))
     * 空间复杂度：O(1)
     */
    public static void method1(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums2, 0, nums1, m, n);
        Arrays.sort(nums1);
    }

    /**
     * 双指针(从前往后)
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(m)
     */
    public static void method2(int[] nums1, int m, int[] nums2, int n) {
        int[] nums1Copy = new int[m];
        System.arraycopy(nums1, 0, nums1Copy, 0, m);

        // two get pointers for mus1Copy and nums2;
        int p1 = 0;
        int p2 = 0;

        // set pointer for nums1
        int p = 0;

        //compare elements from nums1Copy and nums2 and add the smallest one into nums1;
        while ((p1 < m) && (p2 < n))
            nums1[p++] = (nums1Copy[p1] < nums2[p2]) ? nums1Copy[p1++] : nums2[p2++];

        // if there are still elements to add
        if (p1 < m)
            System.arraycopy(nums1Copy, p1, nums1, p1 + p2, m + n -p1 - p2);
        if (p2 < n)
            System.arraycopy(nums2, p2, nums1, p1 + p2, m + n -p1 - p2);
    }

    /**
     * 双指针(从后往前)
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(1)
     */
    public static void method3(int[] nums1, int m, int[] nums2, int n) {
        // two get pointers for mus1Copy and nums2;
        int p1 = m - 1;
        int p2 = n - 1;

        // set pointer for nums1
        int p = m + n - 1;

        //compare elements from nums1 and nums2 and add the largest one into nums1;
        while ((p1 >= 0) && (p2 >= 0))
            nums1[p--] = (nums1[p1] < nums2[p2]) ? nums2[p2--] : nums1[p1--];

        // add missing elements from nums2
        //System.arraycopy(nums2, 0, nums1, 0, p2 + p1);
    }
}
