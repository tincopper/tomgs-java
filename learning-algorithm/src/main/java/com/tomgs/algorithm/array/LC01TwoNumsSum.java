package com.tomgs.algorithm.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * TwoNumsSum
 * <p>
 * <a href="https://leetcode.cn/problems/two-sum/description/">1. 两数之和</a>
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * <p>
 * 你可以按任意顺序返回答案。
 *
 * @author tomgs
 * @since 2021/12/22
 */
public class LC01TwoNumsSum {

    public static int[] twoSum(int[] nums, int target) {
        if (nums.length < 2) {
            throw new IllegalArgumentException("nums length must >= 2.");
        }
        int[] result = new int[2];
        Map<Integer, Integer> cache = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (cache.get(nums[i]) == null) {
                int secVal = target - nums[i];
                cache.put(secVal, i);
            } else {
                Integer index = cache.get(nums[i]);
                result[0] = index;
                result[1] = i;
                return result;
            }
        }

        return result;
    }

    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer index = map.get(target - nums[i]);
            if (index != null) {
                return new int[] {i, index};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    public static void main(String[] args) {
        int[] nums = {3,2,4};
        int target = 6;

        int[] ints = twoSum(nums, target);
        System.out.println(Arrays.toString(ints));
    }

}
