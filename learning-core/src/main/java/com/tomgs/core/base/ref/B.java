package com.tomgs.core.base.ref;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/4 1.0 
 */
public class B {

    public B() {
        System.out.println("B......");
    }

    /**
     * 测试java是引用传递还是值传递
     */
    public static void main(String[] args) {
        //基本数据类型
        int a = 1;
        int b = 2;
        System.out.println("交换前：" + a + "==" + b);
        swap(a, b);
        System.out.println("交换后：" + a + "==" + b);
        System.out.println("===============================");

        //引用数据类型
        A aRef = new A();
        System.out.println(aRef);
        swapRef(aRef);
        System.out.println(aRef);
        System.out.println("===============================");

        //引用数据类型
        A aRef1 = new A();
        System.out.println(aRef1);
        updateRef(aRef1);
        System.out.println(aRef1);
    }

    private static void updateRef(A aRef1) {
        aRef1.setA(2);
        System.out.println("交换引用：" + aRef1);
    }

    private static void swapRef(A aRef) {
        aRef = new A();
        System.out.println("交换引用：" + aRef);
    }

    private static void swap(int a, int b) {
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("交换后的值a=" + a + ",b=" + b);
    }

}
