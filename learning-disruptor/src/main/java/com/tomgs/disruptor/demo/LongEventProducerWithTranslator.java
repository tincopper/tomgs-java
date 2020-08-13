package com.tomgs.disruptor.demo;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;

/**
 * 使用 translator的方式生产数据
 *
 * @author tomgs
 * @since 2020/8/13
 */
public class LongEventProducerWithTranslator {

  private final RingBuffer<LongEvent> ringBuffer;

  public LongEventProducerWithTranslator(final RingBuffer<LongEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
    @Override
    public void translateTo(LongEvent event, long sequence, ByteBuffer byteBuffer) {
      event.setValue(byteBuffer.get(0));
    }
  };

  public void onData(ByteBuffer bb) {
    ringBuffer.publishEvent(TRANSLATOR, bb);
  }

}
