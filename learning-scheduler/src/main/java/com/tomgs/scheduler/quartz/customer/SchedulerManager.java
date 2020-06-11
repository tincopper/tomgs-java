package com.tomgs.scheduler.quartz.customer;

import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import com.tomgs.scheduler.quartz.customer.extension.ExtensionLoader;

/**
 * @author tangzy
 * @since 1.0
 */
public final class SchedulerManager {

  public static final SchedulerManager INSTANCE = new SchedulerManager();

  private final ExtensionLoader<BasicScheduler> extensionLoader;

  private SchedulerManager() {
    extensionLoader = ExtensionLoader.getExtensionLoader(BasicScheduler.class, SchedulerConfig.class);
  }

  public BasicScheduler createScheduler(SchedulerConfig config) {
    return extensionLoader.getProtoJoin("scheduler", config);
  }

  public BasicScheduler createScheduler(String schedulerName, SchedulerConfig config) {
    return extensionLoader.getProtoJoin(schedulerName, config);
  }

}
