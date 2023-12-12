package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * LC303NumArray
 * <a href="https://leetcode.cn/problems/range-sum-query-immutable/">303. 区域和检索 - 数组不可变</a>
 * <p>
 * 给定一个整数数组  nums，处理以下类型的多个查询:
 * <p>
 * 计算索引 left 和 right （包含 left 和 right）之间的 nums 元素的 和 ，其中 left <= right
 * 实现 NumArray 类：
 * <p>
 * NumArray(int[] nums) 使用数组 nums 初始化对象
 * int sumRange(int i, int j) 返回数组 nums 中索引 left 和 right 之间的元素的 总和 ，包含 left 和 right 两点（也就是 nums[left] + nums[left + 1] + ... + nums[right] )
 *
 *
 * @author tomgs
 * @since 1.0
 */
public class LC303NumArray {

    class NumArray {

        //private int[] nums;

        // 前缀和
        private int[] preSums;

        public NumArray(int[] nums) {
            //this.nums = nums;
            // preSum[0] = 0，便于计算累加和
            preSums = new int[nums.length + 1];
            for (int i = 1; i < preSums.length; i++) {
                preSums[i] = preSums[i - 1] + nums[i - 1];
            }
        }

        public int sumRange(int left, int right) {
            // O(n) 复杂度
//            int res = 0;
//            for (int i = left; i <= right; i++) {
//                res += nums[i];
//            }
//            return res;

            // O(1)复杂度
            // 包含right边界的，所以需要+1
            return preSums[right + 1] - preSums[left];
        }
    }

    /**
     * Your NumArray object will be instantiated and called as such:
     * NumArray obj = new NumArray(nums);
     * int param_1 = obj.sumRange(left,right);
     */

    @Test
    public void test() {
        NumArray obj = new NumArray(new int[]{-2, 0, 3, -5, 2, -1});
        System.out.println(obj.sumRange(0, 2));
        System.out.println(obj.sumRange(2, 5));
        System.out.println(obj.sumRange(0, 5));
    }

}
