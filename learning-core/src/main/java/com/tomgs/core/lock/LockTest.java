package com.tomgs.core.lock;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/5 1.0 
 */
public class LockTest {

    /*
        多线程之间按顺序调用，实现A->B->C三个线程启动，要求如下：
        AA打印5次，BB打印10次，CC打印15次
        按照这个顺序打印10轮。
     */
    @Test
    public void test1() throws InterruptedException {
        // 线程、操作、资源类，所以需要先定义一个资源类，并且提供并发的操作
        // 判断、干活、做通知，先进行条件判断，然后进行业务处理，处理完之后进行下一步的线程通知
        ShardSource shardSource = new ShardSource();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shardSource.print5();
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shardSource.print10();
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shardSource.print15();
            }
        }, "CC").start();
        //等3s，待结果全部输出
        Thread.sleep(3000);

    }

    /**
     * 1.1、资源类
     */
    class ShardSource {
        ReentrantLock lock = new ReentrantLock();
        int flag = 1; //1 -- AA, 2 -- BB, 3 -- CC
        Condition aa = lock.newCondition();
        Condition bb = lock.newCondition();
        Condition cc = lock.newCondition();

        //1.2、支持线程安全的打印方法
        public void print5() {
            lock.lock();
            try {
                //2.1、判断，是否为A线程
                while (flag != 1) { // 使用while防止虚假唤醒
                    aa.await(); // 如果不是a线程则进行阻塞，否则干活
                }
                //2.2、干活
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                }
                //2.3、通知，执行完后需要通知B线程
                flag = 2;
                bb.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void print10() {
            lock.lock();
            try {
                //2.1、判断，是否为A线程
                while (flag != 2) { // 使用while防止虚假唤醒
                    bb.await(); // 如果不是a线程则进行阻塞，否则干活
                }
                //2.2、干活
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                }
                //2.3、通知，执行完后需要通知B线程
                flag = 3;
                cc.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void print15() {
            lock.lock();
            try {
                //2.1、判断，是否为A线程
                while (flag != 3) { // 使用while防止虚假唤醒
                    cc.await(); // 如果不是a线程则进行阻塞，否则干活
                }
                //2.2、干活
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                }
                //2.3、通知，执行完后需要通知B线程
                flag = 1;
                aa.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
