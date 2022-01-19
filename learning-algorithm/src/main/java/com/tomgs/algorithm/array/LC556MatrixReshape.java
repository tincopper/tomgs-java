package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * 566. 重塑矩阵
 *
 * 在 MATLAB 中，有一个非常有用的函数 reshape ，它可以将一个m x n 矩阵重塑为另一个大小不同（r x c）的新矩阵，但保留其原始数据。
 * 给你一个由二维数组 mat 表示的m x n 矩阵，以及两个正整数 r 和 c ，分别表示想要的重构的矩阵的行数和列数。
 * 重构后的矩阵需要将原始矩阵的所有元素以相同的 行遍历顺序 填充。
 * 如果具有给定参数的 reshape 操作是可行且合理的，则输出新的重塑矩阵；否则，输出原始矩阵。
 * <p>
 * 输入：mat = [[1,2],[3,4]], r = 1, c = 4
 * 输出：[[1,2,3,4]]
 * <p>
 * 输入：mat = [[1,2],[3,4]], r = 2, c = 4
 * 输出：[[1,2],[3,4]]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reshape-the-matrix
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/19 1.0
 */
public class LC556MatrixReshape {

    // 暴力解法
    public int[][] matrixReshape(int[][] mat, int r, int c) {
        int rowLen = mat.length;
        int colLen = mat[0].length;

        if (rowLen * colLen != r * c) {
            return mat;
        }

        int[][] result = new int[r][c];
        int rowIndex = 0, colIndex = 0;
        for (int[] rows : mat) {
            for (int cols : rows) {
                result[rowIndex][colIndex] = cols;
                if (colIndex < c - 1) {
                    colIndex++;
                } else {
                    rowIndex++;
                    colIndex = 0;
                }
            }
        }
        return result;
    }

    /**
     * 标准解法：
     *
     * 思路与算法
     *
     * 对于一个行数为 mm，列数为 nn，行列下标都从 00 开始编号的二维数组，我们可以通过下面的方式，将其中的每个元素 (i, j)(i,j) 映射到整数域内，并且它们按照行优先的顺序一一对应着 [0, mn)[0,mn) 中的每一个整数。形象化地来说，我们把这个二维数组「排扁」成了一个一维数组。如果读者对机器学习有一定了解，可以知道这就是 \texttt{flatten}flatten 操作。
     *
     * 这样的映射即为：
     *
     * (i,j) → i × n + j
     *
     * 同样地，我们可以将整数 xx 映射回其在矩阵中的下标，即
     *
     * {
     *  i = x/n
     *  j = x%n
     * }
     *
     *
     * 其中 / 表示整数除法，% 表示取模运算。
     *
     * 那么题目需要我们做的事情相当于：
     * 将二维数组 nums 映射成一个一维数组；
     * 将这个一维数组映射回 rr 行 cc 列的二维数组。
     *
     * 我们当然可以直接使用一个一维数组进行过渡，但我们也可以直接从二维数组 \textit{nums}nums 得到 rr 行 cc 列的重塑矩阵：
     * 设 nums 本身为 mm 行 nn 列，如果 mn != rc，那么二者包含的元素个数不相同，因此无法进行重塑；
     * 否则，对于 x ∈[0,mn)，第 x 个元素在 nums 中对应的下标为 (x/n,x%n)，
     * 而在新的重塑矩阵中对应的下标为 (x/c, x%c)。我们直接进行赋值即可。
     */
    public int[][] matrixReshape2(int[][] mat, int r, int c) {
        int rowLen = mat.length;
        int colLen = mat[0].length;

        if (rowLen * colLen != r * c) {
            return mat;
        }

        int[][] result = new int[r][c];
        for (int x = 0; x < rowLen * colLen; ++x) {
            result[x / c][x % c] = mat[x / colLen][x % colLen];
        }
        return result;
    }

    @Test
    public void test() {
        int[][] mat = {{1, 2}, {3, 4}};
        int r = 1, c = 4;
        int[][] result = matrixReshape(mat, r, c);
        System.out.println(Arrays.deepToString(result));
    }

    @Test
    public void test2() {
        int[][] mat = {{1, 2}, {3, 4}};
        int r = 2, c = 4;
        int[][] result = matrixReshape(mat, r, c);
        System.out.println(Arrays.deepToString(result));
    }

    @Test
    public void test3() {
        int[][] mat = {{1, 2}, {3, 4}};
        int r = 1, c = 4;
        int[][] result = matrixReshape2(mat, r, c);
        System.out.println(Arrays.deepToString(result));
    }

    @Test
    public void test4() {
        int[][] mat = {{1, 2}, {3, 4}};
        int r = 2, c = 4;
        int[][] result = matrixReshape2(mat, r, c);
        System.out.println(Arrays.deepToString(result));
    }
}
