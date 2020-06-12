package com.tomgs.scheduler.quartz.customer;

import com.google.common.collect.Maps;
import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import com.tomgs.scheduler.quartz.customer.extension.ExtensionLoader;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tangzy
 * @since 1.0
 */
public final class SchedulerManager {

  public static final SchedulerManager INSTANCE = new SchedulerManager();

  private final ReentrantLock lock;
  private final Map<String, BasicScheduler> activeSchedulerMap;
  private final Map<String, BasicScheduler> standBySchedulerMap;
  private final ExtensionLoader<BasicScheduler> extensionLoader;

  private SchedulerManager() {
    lock = new ReentrantLock();
    activeSchedulerMap = Maps.newConcurrentMap();
    standBySchedulerMap = Maps.newConcurrentMap();
    extensionLoader = ExtensionLoader.getExtensionLoader(BasicScheduler.class, SchedulerConfig.class);
  }

  public BasicScheduler createScheduler(SchedulerConfig config) {
    return extensionLoader.getProtoJoin("scheduler", config);
  }

  public BasicScheduler createScheduler(String schedulerName, SchedulerConfig config) {
    return extensionLoader.getProtoJoin(schedulerName, config);
  }

  public void addActiveScheduler(BasicScheduler scheduler) {
    lock.lock();
    try {
      standBySchedulerMap.remove(scheduler.getSchedulerName());
      activeSchedulerMap.putIfAbsent(scheduler.getSchedulerName(), scheduler);
      if (!scheduler.isStarted()) {
        scheduler.start();
      }
    } finally {
      lock.unlock();
    }
  }

  public void addStandByScheduler(BasicScheduler scheduler) {
    lock.lock();
    try {
      activeSchedulerMap.remove(scheduler.getSchedulerName());
      standBySchedulerMap.putIfAbsent(scheduler.getSchedulerName(), scheduler);
      if (scheduler.isStarted()) {
        scheduler.stop();
      }
    } finally {
      lock.unlock();
    }
  }

}
