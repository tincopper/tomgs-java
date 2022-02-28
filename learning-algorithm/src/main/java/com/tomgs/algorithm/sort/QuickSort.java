package com.tomgs.algorithm.sort;

/**
 *  快速排序
 *
 * @author tomgs
 * @version 2019/11/8 1.0 
 */
public class QuickSort {

    public static void main(String[] args) {

    }

    // 王道考研书上看到的快排算法，利用哨兵减少了交换两个元素的复杂步骤，效果更好一些
    private static void quickSort(int[] a, int head, int tail) {

        int low = head;
        int high = tail;
        int pivot = a[low];
        if (low < high) {

            while (low<high) {
                while (low < high && pivot <= a[high]) high--;
                a[low] = a[high];
                while (low < high && pivot >= a[low]) low++;
                a[high]=a[low];
            }
            a[low] = pivot;

            if(low>head+1) quickSort(a,head,low-1);
            if(high<tail-1) quickSort(a,high+1,tail);
        }

    }
}
