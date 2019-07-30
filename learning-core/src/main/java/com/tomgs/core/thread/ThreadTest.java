package com.tomgs.core.thread;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author tangzhongyuan
 * @since 2019-07-26 10:42
 **/
public class ThreadTest {

    /**
     * 多线程，5个线程内部打印hello和word，hello在前，要求提供一种方法使得5个线程先全部打印出hello后再打印5个word
     *
     */
    @Test
    public void testPrint() throws InterruptedException, IOException {
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                System.out.println("hello");
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("world");
            }, String.valueOf(i));
            t.start();
            //等待线程初始化完成
            Thread.sleep(10);
        }
        latch.countDown();

        //阻塞方便看到输出结果
        Thread.sleep(100);
        //System.in.read();
    }

    /**
     * 三线程按顺序交替打印ABC
     *
     * https://www.jianshu.com/p/f79fa5aafb44
     */
    @Test
    public void test2() {

    }

    /**
     * Java如何让两个线程交替打印奇数和偶数
     *
     * 通过notify和wait实现，如果是奇数或者偶数则打印并调用notify，否则调用wait释放锁等待。
     * 也可以使用ReentrantLock结合Condition实现这个效果。
     */
    @Test
    public void test3() throws IOException {
        final OddEventPrint print = new OddEventPrint();
        Thread thread = new Thread(print, "a");
        Thread thread2 = new Thread(print, "b");
        thread.start();
        thread2.start();

        System.in.read();
    }
}
