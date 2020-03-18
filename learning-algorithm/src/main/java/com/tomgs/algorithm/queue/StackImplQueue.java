package com.tomgs.algorithm.queue;

import java.util.Stack;

/**
 *  用栈实现队列
 *  首先需要了解栈和队列的特性
 *
 * @author tomgs
 * @version 2019/11/8 1.0 
 */
public class StackImplQueue {

    //用于入队列
    private Stack<Integer> stack1 = new Stack<>();
    //用于出队列
    private Stack<Integer> stack2 = new Stack<>();

    /**
     * 入队
     */
    public void push(Integer integer) {
        stack1.push(integer);
    }

    /**
     * 出队
     */
    public Integer pop() {
        //判断栈是否为空
        if (stack1.empty() && stack2.empty()) {
            throw new RuntimeException("queue is empty");
        }
        // 否则将栈1的元素添加到栈2中去，并进行出栈操作
        if (stack2.empty()) {
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

    public static void main(String[] args) {
        StackImplQueue stackImplQueue = new StackImplQueue();
        stackImplQueue.push(1);
        stackImplQueue.push(2);
        stackImplQueue.push(3);

        System.out.println(stackImplQueue.pop());
        System.out.println(stackImplQueue.pop());
        System.out.println(stackImplQueue.pop());
    }

}
