package com.tomgs.scheduler.quartz.customer;

import java.util.ServiceLoader;

/**
 * @author tangzy
 * @since 1.0
 */
public class Main {

  public static void main(String[] args) throws Exception {
    ServiceLoader<BasicScheduler> schedulers = ServiceLoader.load(BasicScheduler.class);
    BasicScheduler scheduler = schedulers.iterator().next();
    DemoJob job = new DemoJob();
    scheduler.addJob(job);
    scheduler.start();
    System.out.println("---------------start-------------");
  }

}
