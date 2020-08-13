package com.tomgs.disruptor.demo;

import com.lmax.disruptor.EventHandler;

/**
 * @author tomgs
 * @since 2020/8/13
 */
public class LongEventHandler implements EventHandler<LongEvent> {

  @Override
  public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
    System.out.println(longEvent.getValue());
  }

}
