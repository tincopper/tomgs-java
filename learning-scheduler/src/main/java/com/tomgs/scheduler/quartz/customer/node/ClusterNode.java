package com.tomgs.scheduler.quartz.customer.node;

import com.google.common.collect.Lists;
import com.tomgs.scheduler.quartz.customer.BasicScheduler;
import com.tomgs.scheduler.quartz.customer.JobInfo;
import java.util.List;

/**
 * 通过hash的方式将任务分配到对应的调度节点，然后调度节点与真实的节点建立关联，可以通过调度节点找到对应的真实节点，同时可以在真实节点上面查询到调度节点列表
 * 任务通过hash的方式去找到对应的scheduler，通过scheduler找到对应执行节点
 * @author tangzy
 * @since 1.0
 */
public class ClusterNode {

  List<Node> nodeList = Lists.newArrayList(new Node("node1", "127.0.0.1:8080"),
      new Node("node2", "127.0.0.1:8081"),
      new Node("node3", "127.0.0.1:8082"));

  // 任务与调度节点映射
  List<BasicScheduler> schedulers = Lists.newArrayListWithCapacity(8);

  List<JobInfo> jobInfoList = Lists.newArrayList();

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
    // 根据具体的节点来完成任务节点的映射。。。。
    // 先弄一些固定数量的虚拟节点，把任务往这些虚拟节点上面加，这样就可以避免物理节点动态上下线引起大量任务的迁移
    int nums = getNodeListLen();
    int size = jobInfoList.size();
    // 根据这两个值去分配任务
    int r = size / nums;
    if (r <= nums) {
      // 任务比节点少，那么直接放在第一个节点即可。
    } else {

    }

    // 根据下标获取任务

    int i = schedulers.size() / nums;
    // 0-2, 3-5, 6-7

  }

  /**
   * 获取当前可用节点数，这个可以通过节点之间互相通信进行统计
   */
  private int getNodeListLen() {
    return 3;
  }

  private List<Node> getNodeList() {
    return nodeList;
  }

}
