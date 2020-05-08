package com.tomgs.algorithm.base;

/**
 * 位移操作实现加减乘除
 * https://www.cnblogs.com/paxing/p/10452264.html
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

  int multiply(int a, int b) {
    //将乘数和被乘数都取绝对值
    int multiplicand = a < 0 ? add(~a, 1) : a; 
    int multiplier = b < 0 ? add(~b , 1) : b;
    
    //计算绝对值的乘积
    int product = 0;
    while(multiplier > 0) {
      if((multiplier & 0x1) > 0) {// 每次考察乘数的最后一位
        product = add(product, multiplicand);
      } 
      multiplicand = multiplicand << 1;// 每运算一次，被乘数要左移一位
      multiplier = multiplier >> 1;// 每运算一次，乘数要右移一位（可对照上图理解）
    } 
    //计算乘积的符号
    if((a ^ b) < 0) {
      product = add(~product, 1);
    } 
    return product;
  }

  /*
   * a : 被除数
   * b : 除数
   */
  int divide(int a, int b){
    // 先取被除数和除数的绝对值
    int dividend = a > 0 ? a : add(~a, 1);
    int divisor = b > 0 ? a : add(~b, 1);

    int quotient = 0;// 商
    int remainder = 0;// 余数
    // 不断用除数去减被除数，直到被除数小于被除数（即除不尽了）
    while(dividend >= divisor){// 直到商小于被除数
      quotient = add(quotient, 1);
      dividend = substract(dividend, divisor);
    }
    // 确定商的符号
    if((a ^ b) < 0){// 如果除数和被除数异号，则商为负数
      quotient = add(~quotient, 1);
    }
    // 确定余数符号
    remainder = b > 0 ? dividend : add(~dividend, 1);
    return quotient;// 返回商
  }

  /**
   * 除法
   * @param a
   * @param b
   * @return
   */
  int divide_v2(int a,int b) {
    // 先取被除数和除数的绝对值
    int dividend = a > 0 ? a : add(~a, 1);
    int divisor = b > 0 ? a : add(~b, 1);
    int quotient = 0;// 商
    int remainder = 0;// 余数
    for(int i = 31; i >= 0; i--) {
      //比较dividend是否大于divisor的(1<<i)次方，不要将dividend与(divisor<<i)比较，而是用(dividend>>i)与divisor比较，
      //效果一样，但是可以避免因(divisor<<i)操作可能导致的溢出，如果溢出则会可能dividend本身小于divisor，但是溢出导致dividend大于divisor
      if((dividend >> i) >= divisor) {
        quotient = add(quotient, 1 << i);
        dividend = substract(dividend, divisor << i);
      }
    }
    // 确定商的符号
    if((a ^ b) < 0){
      // 如果除数和被除数异号，则商为负数
      quotient = add(~quotient, 1);
    }
    // 确定余数符号
    remainder = b > 0 ? dividend : add(~dividend, 1);
    return quotient;// 返回商
  }

  public static void main(String[] args) {
    System.out.println(substract(8, 1));
    System.out.println(substract(3, -2));
  }

}
