package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.ArrayList;
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
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            if (i == 1) {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(1);
                result.add(tmp);
                continue;
            }
            if (i == 2) {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(1);
                tmp.add(1);
                result.add(tmp);
                continue;
            }
            List<Integer> tmp = new ArrayList<>();
            List<Integer> preLayer = result.get(i - 2);
            tmp.add(1);
            for (int j = 1; j < i - 1; j++) {
                tmp.add(preLayer.get(j - 1) + preLayer.get(j));
            }
            tmp.add(1);
            result.add(tmp);
        }

        return result;
    }

    public List<List<Integer>> generate2(int numRows) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        for (int i = 0; i < numRows; ++i) {
            List<Integer> row = new ArrayList<Integer>();
            for (int j = 0; j <= i; ++j) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    row.add(ret.get(i - 1).get(j - 1) + ret.get(i - 1).get(j));
                }
            }
            ret.add(row);
        }
        return ret;
    }

    @Test
    public void test() {
        List<List<Integer>> generate = generate(5);
        System.out.println(generate);
    }

    @Test
    public void test1() {
        List<List<Integer>> generate = generate(2);
        System.out.println(generate);
    }

    @Test
    public void test2() {
        List<List<Integer>> generate = generate2(5);
        System.out.println(generate);
    }

    @Test
    public void test3() {
        List<List<Integer>> generate = generate2(1);
        System.out.println(generate);
    }

}
