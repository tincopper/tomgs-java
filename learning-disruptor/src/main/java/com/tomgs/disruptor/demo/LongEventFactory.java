package com.tomgs.disruptor.demo;

import com.lmax.disruptor.EventFactory;

/**
 * @author tomgs
 * @since 2020/8/13
 */
public class LongEventFactory implements EventFactory<LongEvent> {

  @Override
  public LongEvent newInstance() {
    return new LongEvent();
  }

}
