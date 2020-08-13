package com.tomgs.disruptor.demo;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.nio.ByteBuffer;

/**
 * @author tomgs
 * @since 2020/8/13
 */
public class LongEventMain {

  public static void main(String[] args) throws InterruptedException {
    LongEventFactory factory = new LongEventFactory();
    Disruptor<LongEvent> disruptor = new Disruptor<>(factory, 1024, DaemonThreadFactory.INSTANCE);
    disruptor.handleEventsWith(new LongEventHandler());
    // Start the Disruptor, starts all threads running
    disruptor.start();

    // Get the ring buffer from the Disruptor to be used for publishing.
    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
    LongEventProducer longEventProducer = new LongEventProducer(ringBuffer);

    ByteBuffer bb = ByteBuffer.allocate(8);
    for (long l = 0; true; l++) {
      bb.putLong(0, l);
      // 生产数据
      longEventProducer.onData(bb);
      Thread.sleep(1000);
    }

  }

}
