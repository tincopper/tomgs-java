package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.List;

/**
 * 118. 杨辉三角
 *
 * 给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
 *
 * 在「杨辉三角」中，每个数是它左上方和右上方的数的和。
 *
 * 输入: numRows = 5
 * 输出: [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
 *
 * 输入: numRows = 1
 * 输出: [[1]]
 *
 * @author tomgs
 * @version 2022/1/19 1.0
 */
public class LC118YangHuiTriangle {

    public List<List<Integer>> generate(int numRows) {
        return null;
    }

    @Test
    public void test() {
        List<List<Integer>> generate = generate(5);
        System.out.println(generate);
    }


    @Test
    public void test1() {
        List<List<Integer>> generate = generate(1);
        System.out.println(generate);
    }

}
