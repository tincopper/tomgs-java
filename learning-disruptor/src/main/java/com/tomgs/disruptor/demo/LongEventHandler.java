package com.tomgs.disruptor.demo;

import com.lmax.disruptor.EventHandler;

/**
 * @author tomgs
 * @since 2020/8/13
 */
public class LongEventHandler implements EventHandler<LongEvent> {

  private String name;

  @Override
  public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
    System.out.println(Thread.currentThread().getName() + ": " + getName() + ": " + longEvent.getValue());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
