package com.tomgs.netty.test;

/**
 * @author tangzhongyuan
 * @create 2019-04-20 18:39
 **/
public class NettyServerBootstrap {

    public static void main(String[] args) {
        int i = 0 & ~16;
        System.out.println(i);
        // 1000
        int i1 = ~8;
        System.out.println(i1);

        // a & ~a = 0
        // a & ~b = a
        int i2 = 8 & ~8;
        System.out.println(i2);

        // a & a = a
        // b & a = 0
        int i3 = 8 & 8;
        System.out.println(i3);

        // a ^ a = 0
        // a ^ b != 0
        int i4 = 8 ^ 8;
        System.out.println(i4);
    }

}
