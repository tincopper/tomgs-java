package com.tomgs.disruptor.demo;

import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;

/**
 * 普通的方式生产数据
 *
 * @author tomgs
 * @since 2020/8/13
 */
public class LongEventProducer {

  private final RingBuffer<LongEvent> ringBuffer;

  public LongEventProducer(final RingBuffer<LongEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void onData(ByteBuffer byteBuffer) {
    long sequence = ringBuffer.next();
    try {
      LongEvent longEvent = ringBuffer.get(sequence);
      longEvent.setValue(byteBuffer.getLong(0)); //填充数据
    } finally {
      // 发布事件
      ringBuffer.publish(sequence);
    }
  }

}
