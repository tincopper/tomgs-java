package com.tomgs.algorithm.array;

/**
 * 在整型有序的数组中查找一个数字的下标
 *
 * 使用折半查找
 *
 * @author tomgs
 * @version 2019/11/21 1.0
 */
public class FindIndexFromArray {

    public static void main(String[] args) {
        char[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int to_find = 6;                   //to_find是需要寻找的数字

        int result = findIndex(a, to_find);
        System.out.println(result);
    }

    public static int findIndex(char[] a, int num) {
        int n = a.length;                  //n代表总个数
        int left = 0;
        int right = n - 1;                 //left和right代表的都是数组的下标
        while (true) {
            if (num > a[(left + right) / 2]) //如果寻找的值比中间的值大，就把中间值的下标赋给左边
            {
                left = (left + right) / 2 + 1;
            } else if (num < a[(left + right) / 2]) ////如果寻找的值比中间的值大，就把中间值的下标赋给右边
            {
                right = (left + right) / 2 - 1;
            } else
                break;
        }
        return (left + right) / 2;
    }

}
