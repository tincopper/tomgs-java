package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.List;

/**
 * 15. 三数之和
 * 给你一个包含n个整数的数组nums，判断nums中是否存在三个元素a，b，c ，使得a + b + c =0 ？请你找出所有和为0且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组。
 * <p>
 * 示例 1：
 * 输入：nums = [-1,0,1,2,-1,-4] 输出：[[-1,-1,2],[-1,0,1]]
 * <p>
 * 示例 2：
 * 输入：nums = [] 输出：[]
 * <p>
 * 示例 3：
 * 输入：nums = [0] 输出：[]
 *
 * @author tomgs
 * @since 2022/1/25
 */
public class LC15ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        return null;
    }

    @Test
    public void test() {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result = threeSum(nums);
        System.out.println(result);
    }

}
