package com.tomgs.core.oom;

/**
 *  创建线程过多导致OOM
 *
 *  java.lang.OutOfMemoryError: unable to create new native thread
 *
 * @author tomgs
 * @version 2019/11/10 1.0 
 */
public class UnableCreateNewThreadDemo {

    public static void main(String[] args) {
        for (int i = 1; ; i++) {
            System.out.println("****** i = " + i);
            new Thread(() -> {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "" + i).start();
        }
    }
}
