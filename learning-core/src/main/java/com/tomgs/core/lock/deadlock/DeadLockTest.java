package com.tomgs.core.lock.deadlock;

/**
 *  死锁演示
 *  指两个或者两个以上的线程在执行过程中，因争夺资源而造成的一种互相等待的现象，若无外力干涉那它们都将无法推进下去。
 * @author tomgs
 * @version 2019/11/7 1.0 
 */
public class DeadLockTest {

    static class ShardSource extends Thread {
        Object lockA;
        Object lockB;

        ShardSource (Object lockA, Object lockB) {
            this.lockA = lockA;
            this.lockB = lockB;
        }

        @Override
        public void run() {
            synchronized (lockA) {
                System.out.println(Thread.currentThread().getName() + "持有锁" + lockA + "竞争锁" + lockB);
                synchronized (lockB) {
                    System.out.println(Thread.currentThread().getName() + "持有锁" + lockB);
                }
            }
        }
    }

    public static void main(String[] args) {
        Object lockA = new Object();
        Object lockB = new Object();

        new Thread(new ShardSource(lockA, lockB), "ThreadA").start();
        new Thread(new ShardSource(lockB, lockA), "ThreadB").start();
    }

}
