package com.tomgs.algorithm.sort;

import org.junit.Test;

/**
 *
 * @author tangzhongyuan
 * @since 2019-07-29 9:50
 **/
public class SortTest2 {

    /**
     * 有一个只由0，1，2三种元素构成的整数数组，请使用交换、原地排序而不是使用计数进行排序
     *
     * 思路：从第一个元素开始遍历，如果是1则位置不变，如果是0与前面第一个元素交换left++，如果是2与最后元素交换right++，
     * 遍历过程用i做标记，与元素交换的位置用left和right做标记，知道i大于right的值为止。
     * 这样：时间复杂度为O（n），空间复杂度为O（1）
     */
    @Test
    public void test1() {
        int[] arry = {0,1,0,0,1,2,1,2,1};
        final int[] ints = sortThreeColor(arry);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    private int[] sortThreeColor(int[] arry) {
        // write code here
        if (arry.length < 1) {
            return arry;
        }
        int left = 0;
        int right = arry.length - 1;
        int i = 0;
        while (i <= right) {
            if (arry[i] == 0) {
                swap(arry, i, left++);
                i++;
            } else if (arry[i] == 2) {
                swap(arry, i, right--);
            } else
                i++;
        }
        return arry;
    }

    private void swap(int[] A, int a, int b) {
        if (a != b) {
            int temp = A[a];
            A[a] = A[b];
            A[b] = temp;
        }
    }

}
