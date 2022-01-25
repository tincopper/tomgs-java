package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * 11. 盛最多水的容器
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点(i,ai) 。
 * 在坐标内画 n 条垂直线，垂直线 i的两个端点分别为(i,ai) 和 (i, 0) 。
 * 找出其中的两条线，使得它们与x轴共同构成的容器可以容纳最多的水。
 * <p>
 * 说明：你不能倾斜容器。
 * <p>
 * https://aliyun-lc-upload.oss-cn-hangzhou.aliyuncs.com/aliyun-lc-upload/uploads/2018/07/25/question_11.jpg
 * <p>
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为49。
 * <p>
 * 示例 2：
 * <p>
 * 输入：height = [1,1]
 * 输出：1
 * 示例 3：
 * <p>
 * 输入：height = [4,3,2,1,4]
 * 输出：16
 * 示例 4：
 * <p>
 * 输入：height = [1,2,1]
 * 输出：2
 *
 * https://leetcode-cn.com/problems/container-with-most-water/solution/sheng-zui-duo-shui-de-rong-qi-by-leetcode-solution/
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/container-with-most-water
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/24 1.0
 */
public class LC11MaxArea {

    /**
     * 这个数据量大时会超时，因为重复计算了
     * 双指针是想到了，但是右指针的位置，没有考量好
     */
    public int maxArea(int[] height) {
        int maxArea = 0;
        // 左右指针
        int left = 0, right = 1;
        int len = height.length;
        while (left < len - 1) {
            int w = right - left;
            int h = Math.min(height[left], height[right]);
            int size = w * h;
            if (size > maxArea) {
                maxArea = size;
            }
            if (right < len - 1) {
                right++;
            } else {
                left++;
                right = left + 1;
            }
        }

        return maxArea;
    }

    /**
     * 优化版：
     */
    public int maxArea2(int[] height) {
        int maxArea = 0;
        // 左右指针
        int len = height.length;
        int left = 0, right = len - 1;
        while (left < right) {
            int w = right - left;
            int h = Math.min(height[left], height[right]);
            int size = w * h;
            if (size > maxArea) {
                maxArea = size;
            }
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    @Test
    public void test() {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result = maxArea(height);
        int result2 = maxArea2(height);
        System.out.println(result);
        System.out.println(result2);
    }

    @Test
    public void test1() {
        int[] height = {4, 3, 2, 1, 4};
        int result = maxArea(height);
        int result2 = maxArea2(height);
        System.out.println(result);
        System.out.println(result2);
    }

    @Test
    public void test2() {
        int[] height = {1, 1};
        int result = maxArea(height);
        int result2 = maxArea2(height);
        System.out.println(result);
        System.out.println(result2);
    }

    @Test
    public void test3() {
        int[] height = {1, 2, 1};
        int result = maxArea(height);
        int result2 = maxArea2(height);
        System.out.println(result);
        System.out.println(result2);
    }
}
