package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * LC56MergeIntervals
 * <p>
 * <a href="https://leetcode.cn/problems/merge-intervals/">56. 合并区间</a>
 * <p>
 * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
 * 请你合并所有重叠的区间，并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间 。
 * <p>
 * 示例 1：
 * <p>
 * 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
 * 输出：[[1,6],[8,10],[15,18]]
 * 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
 *
 * @author tomgs
 * @since 1.0
 */
public class LC56MergeIntervals {

    // 合并区间解法：
    // 1、排序
    // 2、找重叠（画图）
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new int[][] {};
        }
        // 按区间start 升序排序
        Arrays.sort(intervals, (Comparator.comparingInt(o -> o[0])));
        List<int[]> res = new ArrayList<>();

        // 先放第一个，这个肯定最小，后面就比较右区间和其他左区间的大小
        res.add(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            final int[] end = res.get(res.size() - 1);
            // end1 >= start2
            //    end1 = max(end1, end2)
            if (end[1] >= intervals[i][0]) {
                // 这里要考虑包含关系，所以需要对区间的end取最大值
                end[1] = Math.max(intervals[i][1], end[1]);
            } else {
                res.add(intervals[i]);
            }
        }

        return res.toArray(new int[0][]);
    }

    @Test
    public void test() {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {3, 9}, {15, 18}};
        final int[][] result = merge(intervals);
        System.out.println(Arrays.deepToString(result));
    }

    @Test
    public void test2() {
        int[][] intervals = {{1, 4}, {2, 3}};
        final int[][] result = merge(intervals);
        System.out.println(Arrays.deepToString(result));
    }

}
