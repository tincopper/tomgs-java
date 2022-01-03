package com.tomgs.core.file;

import java.util.ArrayList;
import java.util.List;

/**
 * DisruptorPool
 *
 * @author tomgs
 * @since 2022/1/3
 */
public class DisruptorPool {

    private final List<ConsumerHandler> list = new ArrayList<>();

    public DisruptorPool() {
        list.add(new ConsumerHandler(0));
        list.add(new ConsumerHandler(1));
        list.add(new ConsumerHandler(2));
        list.add(new ConsumerHandler(3));
    }

    public ConsumerHandler getConsumerHandler() {
        while (true) {
            for (ConsumerHandler consumerHandler : list) {
                if (consumerHandler.isEnd()) {
                    return consumerHandler;
                }
                if (!consumerHandler.isStart()) {
                    consumerHandler.start();
                    return consumerHandler;
                }
            }
        }
    }

    static class ConsumerHandler {

        private final DisruptorCommon<String> queue;

        private boolean isEnd = false;

        private boolean isStart = false;

        public ConsumerHandler(int id) {
            this.queue = new DisruptorCommon<>(8 * 1024, id);
            queue.setDataHandler(element -> {
                try {
                    consumer(element.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        public void start() {
            //thread.start();
            queue.start();
            isStart = true;
        }

        public void consumer(String data) throws InterruptedException {
            if (data.startsWith("\\.")) {
                isEnd = true;
                return;
            }
            if (isEnd) {
                isEnd = false;
            }
            // handler data
            Thread.sleep(1);
            System.out.println(Thread.currentThread().getName() + "--> " + data);
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
