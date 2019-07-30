package com.tomgs.core.thread;

/**
 * @author tangzhongyuan
 * @since 2019-07-26 14:38
 **/
public class OddEventPrint implements Runnable {

    private boolean flag = false;
    private int i = 0;

    @Override
    public void run() {
        oddoreven(Thread.currentThread().getName());
    }

    private synchronized void oddoreven(String name) {
        if (name.equals("a")) {
            while (i < 50) {
                if (!flag) {
                    System.out.println("a" + i);
                    i++;
                    flag = true;
                    notifyAll();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (name.equals("b")) {
            while (i < 50) {
                if (flag) {
                    System.out.println("b" + i);
                    i++;
                    flag = false;
                    notifyAll();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
