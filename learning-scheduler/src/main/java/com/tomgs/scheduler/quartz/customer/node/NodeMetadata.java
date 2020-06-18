package com.tomgs.scheduler.quartz.customer.node;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * @author tangzy
 * @since 1.0
 */
public class NodeMetadata {
  // 对应节点上面的active scheduler的数量
  private Map<Node, Integer> nodeActiveSchedulers = Maps.newHashMap();
  // 节点上面的任务数量
  private Map<Node, Integer> nodeJobs = Maps.newHashMap();
  // 节点任务分配信息

}
