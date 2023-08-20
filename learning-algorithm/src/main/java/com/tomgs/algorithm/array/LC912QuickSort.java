package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * <a href="https://leetcode.cn/problems/sort-an-array/">912. 排序数组</a>
 * <p>
 * 给你一个整数数组 nums，请你将该数组升序排列。
 *
 * @author tomgs
 * @version 1.0
 */
public class LC912QuickSort {

    public int[] sortArray(int[] nums) {
        // 为了避免出现耗时的极端情况，先随机打乱
        shuffle(nums);

        // 排序整个数组（原地修改）
        sort(nums, 0, nums.length - 1);

        return nums;
    }

    // 快速排序模板框架
    private void sort(int[] nums, int lo, int hi) {
        if (lo > hi) {
            return;
        }

        // 对 nums[lo..hi] 进行切分
        // 使得 nums[lo..p-1] <= nums[p] < nums[p+1..hi]
        int p = partition(nums, lo, hi);

        sort(nums, lo, p - 1);
        sort(nums, p + 1, hi);
    }

    private int partition(int[] nums, int lo, int hi) {
        // 找到pivot， 以low的值为pivot值
        int pivot = nums[lo];

        // 关于区间的边界控制需格外小心，稍有不慎就会出错
        // 我这里把 i, j 定义为开区间，同时定义：
        // [lo, i) <= pivot；(j, hi] > pivot
        // 之后都要正确维护这个边界区间的定义
        int i = lo + 1;
        int j = hi;
        // 当 i > j 时结束循环，以保证区间 [lo, hi] 都被覆盖
        while (i <= j) {
            // 小于pivot的在左边，位置不动
            while (i < hi && nums[i] <= pivot) {
                i++;
                // 此 while 结束时恰好 nums[i] > pivot
            }
            // 大于pivot的在右边，位置不动
            while (j > lo && nums[j] > pivot) {
                j--;
                // 此 while 结束时恰好 nums[j] <= pivot
            }
            if (i >= j) {
                break;
            }
            // 此时 [lo, i) <= pivot && (j, hi] > pivot
            // 交换 nums[j] 和 nums[i]
            swap(nums, i, j);
            // 此时 [lo, i] <= pivot && [j, hi] > pivot
        }

        // 最后将 pivot 放到合适的位置，即 pivot 左边元素较小，右边元素较大
        swap(nums, lo, j);
        return j;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private void shuffle(int[] nums) {
        Random rand = new Random();
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // 生成 [i, n - 1] 的随机数
            int r = i + rand.nextInt(n - i);
            swap(nums, i, r);
        }
    }

    // 解法2 https://pdai.tech/md/algorithm/alg-sort-x-fast.html
    private void sortArray2(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        quickSort(nums, l, r);
    }

    private void quickSort(int[] nums, int l, int r) {
        if (l >= r) {
            return;
        }
        // 指定当前轮要排序的数据 pivot
        int i = l;
        int j = r;
        int pivot = nums[i];

        while (i < j) {
            // 先从右往左找，找到小于pivot的数
            while (i < j && nums[j] > pivot) {
                j--;
            }
            if (i < j) {
                nums[i] = nums[j];
                i++;
            }
            // 然后从左往右找，找到大于pivot的数
            while (i < j && nums[i] < pivot) {
                i++;
            }
            if (i < j) {
                nums[j] = nums[i];
                j--;
            }
            // 一直这样先从右 -> 左，左 -> 右 这样循环
        }
        // 此时的i即为排序好的位置
        nums[i] = pivot;

        quickSort(nums, l, i - 1);
        quickSort(nums, i + 1, r);
    }

    @Test
    public void test() {
        int[] nums = {5, 1, 1, 2, 0, 0};
        sortArray(nums);

        System.out.println(Arrays.toString(nums));
    }

    @Test
    public void test2() {
        int[] nums = {30, 40, 60, 10, 20, 50};
        sortArray2(nums);

        System.out.println(Arrays.toString(nums));
    }

}
