package com.tomgs.algorithm.base;

/**
 * 位移操作实现加减乘除
 *
 * @author tangzy
 * @since 1.0
 */
public class BaseTest2 {

  // 迭代写法
  static int add(int num1, int num2){
    int sum = num1 ^ num2;
    int carry = (num1 & num2) << 1;
    while(carry != 0){
      int a = sum;
      int b = carry;
      sum = a ^ b;
      carry = (a & b) << 1;
    }
    return sum;
  }

  /*
   * num1: 减数
   * num2: 被减数
   */
  static int substract(int num1, int num2){
    int subtractor = add(~num2, 1);// 先求减数的补码（取反加一）
    int result = add(num1, subtractor); // add()即上述加法运算　　
    return result ;
  }

  public static void main(String[] args) {
    System.out.println(substract(8, 1));
    System.out.println(substract(3, -2));
  }

}
