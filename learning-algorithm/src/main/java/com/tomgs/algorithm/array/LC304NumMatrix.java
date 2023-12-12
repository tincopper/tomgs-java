package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * LC304NumMatrix
 * <a href="https://leetcode.cn/problems/range-sum-query-2d-immutable/">304. 二维区域和检索 - 矩阵不可变</a>
 * <p>
 * 给定一个二维矩阵 matrix，以下类型的多个请求：
 * <p>
 * 计算其子矩形范围内元素的总和，该子矩阵的 左上角 为 (row1, col1) ，右下角 为 (row2, col2) 。
 * 实现 NumMatrix 类：
 * <p>
 * NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
 * int sumRegion(int row1, int col1, int row2, int col2) 返回 左上角 (row1, col1) 、右下角 (row2, col2) 所描述的子矩阵的元素 总和 。
 *
 * <a href="https://labuladong.github.io/algo/images/%E5%89%8D%E7%BC%80%E5%92%8C/5.jpeg">画图理解矩阵的变化</a>
 * @author tomgs
 * @since 1.0
 */
public class LC304NumMatrix {

    private int[][] preMatrix;

    class NumMatrix {

        public NumMatrix(int[][] matrix) {
            // r 行数，c列数
            int r = matrix.length, c = matrix[0].length;
            if (r == 0 || c == 0) {
                return;
            }
            // +1 是为了方便做累计和
            preMatrix = new int[r + 1][c + 1];

            // (2,1)(4,3) = (0,0)(4,3) - (0,0)(4,1) - (0,0)(2,3) + (0,0)(2,1)
            // 因为 (row1,col1)(row2,col2) = (0,0)(row2,col2) - (0,0)(row2,col1) - (0,0)(row1,col2) + (0,0)(row1,col1)
            // 所以 (0,0)(row2,col2) = (0,0)(row2,col1) + (0,0)(row1,col2) + (row1,col1)(row2,col2) - (0,0)(row1,col1)
            for (int row = 1; row <= r; row++) {
                for (int col = 1; col <= c; col++) {
                    // 计算每个矩阵 [0, 0, i, j] 的元素和
                    // 这个要画图理解一下
                    preMatrix[row][col] = preMatrix[row - 1][col] + preMatrix[row][col - 1] + matrix[row - 1][col - 1] - preMatrix[row - 1][col - 1];
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // (2,1)(4,3) = (0,0)(4,3) - (0,0)(4,1) - (0,0)(2,3) + (0,0)(2,1)
            // (row1,col1)(row2,col2) = (0,0)(row2 + 1,col2 + 1) - (0,0)(row2 + 1,col1) - (0,0)(row1,col2 + 1) + (0,0)(row1,col1)
            // +1 是因为包括边界
            return preMatrix[row2 + 1][col2 + 1] - preMatrix[row2 + 1][col1] - preMatrix[row1][col2 + 1] + preMatrix[row1][col1];
        }
    }

    /**
     * Your NumMatrix object will be instantiated and called as such:
     * NumMatrix obj = new NumMatrix(matrix);
     * int param_1 = obj.sumRegion(row1,col1,row2,col2);
     */
    @Test
    public void test() {
        NumMatrix numMatrix = new NumMatrix(new int[][]{
                new int[]{3, 0, 1, 4, 2},
                new int[]{5, 6, 3, 2, 1},
                new int[]{1, 2, 0, 1, 5},
                new int[]{4, 1, 0, 1, 7},
                new int[]{1, 0, 3, 0, 5}});
        System.out.println(numMatrix.sumRegion(2, 1, 4, 3)); // return 8 (红色矩形框的元素总和)
        System.out.println(numMatrix.sumRegion(1, 1, 2, 2)); // return 11 (绿色矩形框的元素总和)
        System.out.println(numMatrix.sumRegion(1, 2, 2, 4)); // return 12 (蓝色矩形框的元素总和)
    }
}
