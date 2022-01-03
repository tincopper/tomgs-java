package com.tomgs.core.file;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * DisruptorCommon
 *
 * @author tomgs
 * @since 2022/1/3
 */
public class DisruptorCommon<E> {

    // 队列中的元素
    static class Element<E> {
        private E value;

        public E get() {
            return value;
        }

        public void set(E value) {
            this.value = value;
        }
    }

    private final Disruptor<Element<E>> disruptor;

    public DisruptorCommon(int bufferSize, int threadId) {
        // 生产者的线程工厂
        ThreadFactory threadFactory = r -> new Thread(r, "disruptor-thread-" + threadId);
        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element<E>> factory = new EventFactory<Element<E>>() {
            @Override
            public Element<E> newInstance() {
                return new Element<>();
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();
        // 指定RingBuffer的大小
        //int bufferSize = 8 * 1024;
        // 创建disruptor，采用单生产者模式
        this.disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);
    }

    public void setDataHandler(Consumer<Element<E>> consumer) {
        // 处理Event的handler
        EventHandler<Element<E>> handler = new EventHandler<Element<E>>() {
            @Override
            public void onEvent(Element<E> element, long sequence, boolean endOfBatch) {
                consumer.accept(element);
            }
        };
        // 设置EventHandler
        disruptor.handleEventsWith(handler);
    }

    public void start() {
        // 启动disruptor的线程
        disruptor.start();
    }

    public void put(E data) {
        RingBuffer<Element<E>> ringBuffer = disruptor.getRingBuffer();
        // 获取下一个可用位置的下标
        long sequence = ringBuffer.next();
        try {
            // 返回可用位置的元素
            Element<E> event = ringBuffer.get(sequence);
            // 设置该位置元素的值
            event.set(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public static void main(String[] args) throws Exception {
        DisruptorCommon<String> disruptorCommon = new DisruptorCommon<>(8 * 1024, 0);
        disruptorCommon.setDataHandler(element -> {
            System.out.println(element.get());
        });
        disruptorCommon.start();

        for (int l = 0; true; l++) {
            disruptorCommon.put("d: " + l);
            Thread.sleep(10);
        }
    }

}
