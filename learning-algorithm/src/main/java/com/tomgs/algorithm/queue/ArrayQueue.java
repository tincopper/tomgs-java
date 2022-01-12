package com.tomgs.algorithm.queue;

/**
 * 用数组实现队列
 *
 * @author tomgs
 * @version 2022/1/12 1.0
 */
public class ArrayQueue {
    // 数组存放队列元素
    private Object[] elements;

    // 队列的头节点位置
    private int head;

    // 队列的尾节点位置
    private int tail;

    // 队列大小
    private int size;

    public ArrayQueue(int size) {
        this.size = size;
        this.elements = new Object[size];
    }

    /**
     * 入队列
     *
     * @param element 待入队元素
     * @return 入队是否成功
     */
    public boolean enqueue(Object element) {
        // 队列已经满了
        if (tail == size) {
            return false;
        }
        elements[tail++] = element;
        return true;
    }

    /**
     * 出队列
     *
     * @return 出队列元素
     */
    public Object dequeue() {
        // 判断队列是否为空
        if (head == tail) {
            return null;
        }
        Object element = elements[head];
        elements[head] = null;
        head++;
        return element;
    }

    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue(3);
        for (int i = 0; i < 4; i++) {
            boolean enqueue = arrayQueue.enqueue(i);
            System.out.println(enqueue);
        }
        for (int i = 0; i < 4; i++) {
            System.out.println(arrayQueue.dequeue());
        }
    }

}
