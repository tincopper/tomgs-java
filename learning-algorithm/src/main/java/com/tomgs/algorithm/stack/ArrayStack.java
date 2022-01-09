package com.tomgs.algorithm.stack;

/**
 * 数组方式实现顺序栈
 *
 * @author tomgs
 * @version 2022/1/8 1.0
 */
public class ArrayStack {

    /**
     * 存放栈元素的数组
     */
    private String[] stack;

    /**
     * 栈中元素个数
     */
    private int count;

    /**
     * 栈的长度
     */
    private int length;

    public ArrayStack(int length) {
        this.count = 0;
        this.length = length;
        this.stack = new String[length];
    }

    public boolean push(String ele) {
        if (count == length) {
            return false;
        }
        stack[count++] = ele;
        return true;
    }

    public String pop() {
        if (count == 0) {
            return null;
        }
        return stack[--count];
    }

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack(6);
        for (int i = 0; i < 6; i++) {
            stack.push("e:" + i);
        }
        for (int i = 0; i < 6; i++) {
            System.out.println(stack.pop());
        }
    }

}
