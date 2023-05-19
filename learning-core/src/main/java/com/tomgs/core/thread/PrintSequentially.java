package com.tomgs.core.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PrintSequentially
 * <p>
 * Multithreaded sequential printing
 * </p>
 * 三个线程按顺序打印有那几种方法
 *
 * @author tomgs
 * @since 1.0
 */
public class PrintSequentially {

    private static final Object lock = new Object();
    private volatile int currentThread = 1;

    private static final CountDownLatch latch1 = new CountDownLatch(1);
    private static final CountDownLatch latch2 = new CountDownLatch(1);

    /**
     * 使用synchronized关键字和wait()/notify()方法
     */
    @Test
    public void test1() {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (currentThread != 1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Thread 1");
                currentThread = 2;
                lock.notifyAll();
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                while (currentThread != 2) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Thread 2");
                currentThread = 3;
                lock.notifyAll();
            }
        });

        Thread t3 = new Thread(() -> {
            synchronized (lock) {
                while (currentThread != 3) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Thread 3");
                currentThread = 1;
                lock.notifyAll();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 使用CountDownLatch类
     */
    @Test
    public void test2() {
        Thread t1 = new Thread(() -> {
            System.out.println("Thread1");
            latch1.countDown();
        });

        Thread t2 = new Thread(() -> {
            try {
                latch1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread2");
            latch2.countDown();
        });

        Thread t3 = new Thread(() -> {
            try {
                latch2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread3");
        });

        t1.start();
        t2.start();
        t3.start();
    }

    private Semaphore semaphore1 = new Semaphore(0);
    private Semaphore semaphore2 = new Semaphore(0);
    /**
     * 使用Semaphore类
     */
    @Test
    public void test3() {
        Thread t1 = new Thread(() -> {
            System.out.println("Thread1");
            semaphore1.release();
        });

        Thread t2 = new Thread(() -> {
            try {
                semaphore1.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread2");
            semaphore2.release();
        });

        Thread t3 = new Thread(() -> {
            try {
                semaphore2.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread3");
        });

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 使用Join方法
     */
    @Test
    public void test4() {
        Thread t1 = new Thread(() -> {
            System.out.println("Thread1");
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread2");
        });

        Thread t3 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread3");
        });

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 使用volatile变量
     */
    @Test
    public void test5() {
        Thread t1 = new Thread(() -> {
            while (currentThread == 1) {
                System.out.println("Thread1");
            }
            currentThread = 2;
        });

        Thread t2 = new Thread(() -> {
            while (currentThread == 2) {
                System.out.println("Thread2");
            }
            currentThread = 3;
        });

        Thread t3 = new Thread(() -> {
            while (currentThread == 3) {
                System.out.println("Thread2");
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 使用condition
     */
    @Test
    public void test6() {
        final ReentrantLock lock = new ReentrantLock();
        final Condition condition2 = lock.newCondition();
        final Condition condition3 = lock.newCondition();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread1");
                condition2.signal();
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                condition2.await();
                System.out.println("Thread2");
                condition3.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread t3 = new Thread(() -> {
            lock.lock();
            try {
                condition3.await();
                System.out.println("Thread3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }

}
