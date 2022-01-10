package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.*;

/**
 * 数组相关算法题
 *
 * @author tangzhongyuan
 * @since 2019-07-18 10:34
 **/
public class ArrayTest {

    /**
     * 给定一个整数数组和一个目标值，找出数组中和为目标值的两个数，要求时间复杂度为o(n)
     * 如：数组：1, 2, 7, 5, 4，目标值：8，结果：找到1，7两个值
     */
    @Test
    public void test1() {
        int[] nums = {1, 2, 7, 5, 4};
        int target = 8;

        // 迭代法
        int[] result = findTowSum1(nums, target);
        System.out.println(result[0] + ":" + result[1]);
        // hash表（推荐）
        int[] result2 = findTowSum2(nums, target);
        System.out.println(result2[0] + ":" + result2[1]);
    }

    /**
     * 将元素均为0、1、2的数组排序，时间复杂度O(n)，空间复杂度O(1)。
     */
    @Test
    public void test2() {
        // 1、最简单方式开辟3个数组来存放0，1，2的数据
        // 2、3个指针原地排序，0往前面扔，2往后面扔，1不动
        // 3、快排
        int[] nums = {0, 2, 1, 2, 2, 2, 1, 0};

        int p0 = 0, p1 = 0, p2 = nums.length - 1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                p0++;
            } else if (nums[i] == 1) {
                p1++;
            } else {

            }
        }

        //sort012(nums);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 给你一个有序数组nums，请你原地删除重复出现的元素，使每个元素只出现一次，返回删除后数组的新长度。
     * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
     * eg: nums = [1, 1, 2] output: 2, nums = [1, 2]
     */
    @Test
    public void test3() {
        // 1、有序数组，一个数字可能有多次重复
        int[] nums = {1, 1, 1, 2, 2, 2, 3, 4};

        int length = solution(nums);
        System.out.println("length: " + length);
        System.out.println("nums: " + Arrays.toString(nums));
    }

    /**
     * 217. 给定一个整数数组，判断是否存在重复元素。
     * 如果存在一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。
     *
     * 输入: [1,2,3,1]
     * 输出: true
     *
     * 输入: [1,2,3,4]
     * 输出: false
     *
     * 输入: [1,1,1,3,3,4,3,2,4,2]
     * 输出: true
     */
    @Test
    public void test4() {
        int[] nums = {1, 2, 3, 1};
        //1、先排序再判断相邻元素，Arrays.sort
        //2、使用hash表，HashSet，存在相同元素时add返回false
        boolean result = containsDuplicate(nums);
        System.out.println("result: " + result);
    }

    /**
     * 53. 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * 子数组 是数组中的一个连续部分。
     *
     * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
     * 输出：6
     * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
     *
     * 如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的 分治法 求解。
     */
    @Test
    public void test5() {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        // 1、贪心算法
        // 2、动态规划
        int result = maxSubArray(nums);
        System.out.println(result);
    }

    public int maxSubArray(int[] nums) {
        return 0;
    }

    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) {
                return true;
            }
        }
        return false;
    }

    private int solution(int[] nums) {
        if (nums == null || nums.length <= 0) {
            return 0;
        }
        int currIndex = 0, nextIndex = 1, length = 1;
        while (nextIndex < nums.length) {
            if (nums[currIndex] == nums[nextIndex]) {
                nums[nextIndex] = -1;
                nextIndex++;
            } else {
                currIndex++;
                nums[currIndex] = nums[nextIndex];
                length++;
            }
        }
        return length;
    }

    void sort012(int[] array) {
        int p0 = 0;
        int p2 = array.length - 1;
        int p1;
        //p0指向第一个不是0的值
        while (array[p0] == 0 && p0 < array.length) {
            p0++;
        }
        //p2指向第一个不是2的值
        while (array[p2] == 2 && p2 >= 0) {
            p2--;
        }
        p1 = p0;//p1从p0的位置开始遍历
        while (p1 <= p2) {
            if (array[p1] == 1)
                p1++;
            else if (array[p1] == 0) {
                swap(array, p0, p1);
                while (array[p0] == 0 && p0 < array.length) {
                    p0++;
                }
            } else {
                swap(array, p2, p1);
                while (array[p2] == 2 && p2 >= 0) {
                    p2--;
                }
            }
        }
    }

    private void swap(int[] array, int i, int i1) {
        int tmp = array[i];
        array[i] = array[i1];
        array[i1] = tmp;
    }

    /**
     * 首先想到的肯定是拿第一个元素与后面的依次加，看是否等于目标值，
     * 然后再第二个、第三个...
     */
    private int[] findTowSum1(int[] nums, int target) {
        final int[] result = new int[2];
        for (int i = 0; i < nums.length - 1; i++) {
            for (int i1 = i + 1; i1 < nums.length; i1++) {
                if (nums[i] + nums[i1] == target && i != i1) {
                    result[0] = i;
                    result[1] = i1;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 把数值作为 key，它的下标作为 value
     * 遍历数组，判断 map 是否含有这个目标值-当前数值，
     * 有直接返回，没有的话放到map里面
     * <p>
     * 所以以后写代码，如果有双层 for 循环，首先考虑一下能否用 map 替换一层
     */
    private int[] findTowSum2(int[] nums, int target) {
        final int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                final Integer index = map.get(nums[i]);
                result[0] = index;
                result[1] = i;
                return result;
            }
            map.put(target - nums[i], i);
        }
        return result;
    }

}
