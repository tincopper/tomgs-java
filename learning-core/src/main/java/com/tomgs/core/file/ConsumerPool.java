package com.tomgs.core.file;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * ConsumerThread
 *
 * @author tomgs
 * @since 2022/1/3
 */
public class ConsumerPool {

    List<ConsumerThread> list = new ArrayList<>();

    public ConsumerPool() {
        list.add(new ConsumerThread(0));
        list.add(new ConsumerThread(1));
        list.add(new ConsumerThread(2));
        list.add(new ConsumerThread(3));
    }

    public ConsumerThread getConsumerThread() {
        while (true) {
            for (ConsumerThread consumerThread : list) {
                if (consumerThread.isEnd()) {
                    return consumerThread;
                }
                if (!consumerThread.isStart()) {
                    consumerThread.start();
                    return consumerThread;
                }
            }
        }
    }
    static class ConsumerThread implements Runnable {

        private final ArrayBlockingQueue<String> queue;

        private final Thread thread;

        private boolean isEnd = false;

        private boolean isStart = false;

        public ConsumerThread(int id) {
            //this.queue = new ConcurrentLinkedDeque<>();
            this.queue = new ArrayBlockingQueue<>(8 * 1024);
            this.thread = new Thread(this, "consumer-" + id);
        }

        public void start() {
            thread.start();
            isStart = true;
        }

        @Override
        public void run() {
            String data;
            while (true) {
                try {
                    data = queue.take();
                    if (isEnd) {
                        isEnd = false;
                    }
                    if (data.startsWith("\\.")) {
                        isEnd = true;
                        continue;
                    }
                    Thread.sleep(1);
                    System.out.println(Thread.currentThread().getName() + "--> " + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void put(String data) throws InterruptedException {
            queue.put(data);
        }

        public boolean isEnd() {
            return isEnd;
        }

        public boolean isStart() {
            return isStart;
        }
    }

}
