package com.tomgs.scheduler.quartz.customer.node;

import com.google.common.collect.Lists;
import com.tomgs.scheduler.quartz.customer.BasicScheduler;
import java.util.List;

/**
 * 通过hash的方式将任务分配到对应的调度节点，然后调度节点与真实的节点建立关联，可以通过调度节点找到对应的真实节点，同时可以在真实节点上面查询到调度节点列表
 * 任务通过hash的方式去找到对应的scheduler，通过scheduler找到对应执行节点
 * @author tangzy
 * @since 1.0
 */
public class ClusterNode {

  // 任务与调度节点映射
  List<BasicScheduler> schedulers = Lists.newArrayListWithCapacity(8);
  // 调度节点与真实节点映射

  public void add(BasicScheduler scheduler) {
    if (schedulers.size() > 8) {
      throw new IndexOutOfBoundsException("scheduler is index out of bound.");
    }
    schedulers.add(scheduler);
  }

  public BasicScheduler get(int index) {
    return schedulers.get(index);
  }

  public void mappingNode() {
    int nums = getNodes();
    int i = schedulers.size() / nums;
    // 0-2, 3-5, 6-7

  }

  /**
   * 获取当前可用节点数，这个可以通过节点之间互相通信进行统计
   */
  private int getNodes() {
    return 3;
  }

}
