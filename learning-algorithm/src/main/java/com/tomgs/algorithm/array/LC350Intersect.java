package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.*;

/**
 * 350. 两个数组的交集 II
 * <p>
 * 给你两个整数数组nums1 和 nums2 ，请你以数组形式返回两数组的交集。
 * 返回结果中每个元素出现的次数，应与元素在两个数组中都出现的次数一致（如果出现次数不一致，则考虑取较小值）。
 * 可以不考虑输出结果的顺序。
 * <p>
 * 输入：nums1 = [1,2,2,1], nums2 = [2,2]
 * 输出：[2,2]
 * <p>
 * 输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * 输出：[4,9]
 * <p>
 * 进阶：
 * <p>
 * 1）如果给定的数组已经排好序呢？你将如何优化你的算法？
 * 2）如果nums1的大小比nums2 小，哪种方法更优？
 * 3）如果nums2的元素存储在磁盘上，内存是有限的，并且你不能一次加载所有的元素到内存中，你该怎么办？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/intersection-of-two-arrays-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/15 1.0
 */
public class LC350Intersect {

    // hash 表法
    public static int[] intersect(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null) {
            return new int[0];
        }
        if (nums1.length > nums2.length) {
            return intersect(nums2, nums1);
        }
        // value -> count
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i : nums1) {
            int count = map.getOrDefault(i, 0) + 1;
            map.put(i, count);
        }

        int index = 0;
        // 最终的数据肯定是小于最小的数组长度
        int[] result = new int[nums1.length];
        for (int num : nums2) {
            if (!map.containsKey(num)) {
                continue;
            }
            Integer count = map.get(num);
            if (count > 0) {
                result[index++] = num;
                count--;
                if (count > 0) {
                    map.put(num, count);
                } else {
                    map.remove(num);
                }
            }
        }
        return Arrays.copyOfRange(result, 0, index);
    }

    // 排序 + 双指针
    public static int[] intersect2(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null) {
            return new int[0];
        }

        // 排序
        Arrays.sort(nums1);
        Arrays.sort(nums2);

        // 最终的数据肯定是小于最小的数组长度
        int count = 0;
        int[] result = new int[Math.min(nums1.length, nums2.length)];
        int index1 = 0, index2 = 0;
        while (index1 < nums1.length && index2 < nums2.length) {
            if (nums1[index1] < nums2[index2]) {
                // nums1当前指针值小于nums2，nums1指针向前移动一位
                index1++;
            } else if (nums1[index1] > nums2[index2]) {
                index2++;
            } else {
                // 相等都往后移一位
                result[count] = nums1[index1];
                index1++;
                index2++;
                count++;
            }
        }
        return Arrays.copyOfRange(result, 0, count);
    }

    @Test
    public void test1() {
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};

        int[] result = intersect(nums1, nums2);
        System.out.println(Arrays.toString(result)); // [2, 2]
    }

    @Test
    public void test2() {
        int[] nums1 = {4, 9, 5};
        int[] nums2 = {9, 4, 9, 8, 4};

        int[] result = intersect(nums1, nums2);
        System.out.println(Arrays.toString(result)); // [4, 9]
    }

    @Test
    public void test3() {
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};

        int[] result = intersect2(nums1, nums2);
        System.out.println(Arrays.toString(result)); // [2, 2]
    }

    @Test
    public void test4() {
        int[] nums1 = {4, 9, 5};
        int[] nums2 = {9, 4, 9, 8, 4};

        int[] result = intersect2(nums1, nums2);
        System.out.println(Arrays.toString(result)); // [4, 9]
    }

}
