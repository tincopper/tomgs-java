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
        int[] array = {0,1,0,0,1,2,1,2,1};
        final int[] ints = sortThreeColor(array);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    private int[] sortThreeColor(int[] array) {
        // write code here
        if (array.length < 1) {
            return array;
        }
        int left = 0;
        int right = array.length - 1;
        int i = 0;
        while (i <= right) {
            if (array[i] == 0) {
                swap(array, i, left++);
                i++;
            } else if (array[i] == 2) {
                swap(array, i, right--);
            } else
                i++;
        }
        return array;
    }

    private void swap(int[] A, int a, int b) {
        if (a != b) {
            int temp = A[a];
            A[a] = A[b];
            A[b] = temp;
        }
    }

}
