package com.tomgs.core.base;

import java.util.Arrays;

/**
 * @author liangcai_zhu
 * @Description TODO
 * @Date 2020/10/30 14:17
 */
public class SortToSide {

  public static void main(String[] args) {
    //int[] arr = {924, 889, 384, 412, 526, 919, 161, 658, 911, 678, 157, 896, 414, 832, 178, 396, 652, 417, 177, 699, 215};
    int[] arr = {3, 2, 5, 4, 5, 1, 7};

    System.out.println(Arrays.toString(arr));
    sortMinAndMax(arr);
    System.out.println(Arrays.toString(arr));
    System.out.println(isSort(arr));

  }

  public static boolean isSort(int[] arr) {
    for (int i = 0; i < arr.length-1; i++) {
      if(arr[i]>arr[i+1]){
        return false;
      }
    }
    return true;
  }

  /**
   * 找最小 往前放 ， 找最大，往后放
   */
  public static void sortMinAndMax(int[] arr) {
    for (int i = 0; i < arr.length >> 1; i++) {
      int last = arr.length - 1 - i;
      minToFirstAndMaxToLast(arr, i, last);
    }


  }

  /**
   * 找最小 往前放 ， 找最大，往后放
   */
  private static void minToFirstAndMaxToLast(int[] arr, int first, int last) {
    if (first >= last) {
      return;
    }
    // 找到最小值的下标
    int minIndex = first;
    int maxIndex = last;

    for (int i = first; i <= last; i++) {
      if (arr[minIndex] > arr[i]) {
        minIndex = i;
      }
      if (arr[maxIndex] < arr[i]) {
        maxIndex = i;
      }
    }

    System.out.println(arr[minIndex] + " - " + arr[maxIndex]);
    swap(arr, first, minIndex);
    swap(arr, last, maxIndex);
  }

  /**
   * 交换 arr 数组中 i 和 j 位置的数据
   *
   * @param arr
   * @param i
   * @param j
   */
  public static void swap(int[] arr, int i, int j) {
    int tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }
}
