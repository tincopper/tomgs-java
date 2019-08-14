package com.tomgs.algorithm.base;

import org.junit.Test;

/**
 * @author tangzhongyuan
 * @since 2019-08-07 18:15
 **/
public class BaseTest {

    /**
     * 获取一个整数值的二进制值
     * 如输入int i = 1 输出 0000 0001
     */
    @Test
    public void test1() {
        int i = 6;
        for (int j = 0; j < 32; j++) {
            /*
             * 0x80000000对应的二进制位 1000 0000 0000 0000 0000 0000 0000 0000
             *
             * 没有无符号左移，因为和左移是一样的
             * 注意的是>>>运算的优先级要高于&运算
             */
            int t = (i & 0x80000000 >>> j) >>> (31 -j);
            System.out.print(t);
        }
    }

    @Test
    public void test2() {
        final String string = Integer.toBinaryString(6);
        System.out.println(string);
    }
}
