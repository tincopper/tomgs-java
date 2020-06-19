package com.tomgs.scheduler.quartz.customer.node;

import com.google.common.collect.Lists;
import com.tomgs.scheduler.quartz.customer.BasicScheduler;
import com.tomgs.scheduler.quartz.customer.JobInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过hash的方式将任务分配到对应的调度节点，然后调度节点与真实的节点建立关联，可以通过调度节点找到对应的真实节点，同时可以在真实节点上面查询到调度节点列表
 * 任务通过hash的方式去找到对应的scheduler，通过scheduler找到对应执行节点
 *
 * 在新增/下线节点时：任务添加成功之后再移除之前旧的
 *
 * 1、启动时先注册节点，然后选举，只有在选举成功之后再做后面的操作
 * 2、这里使用异步：任务分配到对应的槽中0-32（可配置）
 * 3、然后根据注册上来的节点，将槽映射到节点（后续任务的删除新增只需要添加到对应的槽里面即可，无需进行节点之间的迁移，降低了在新增任务和删除任务时的hash不均匀的问题）
 * 4、然后根据节点数量确认每一个节点需要分配调度节点副本数（3台，3个调度对象，一个工作的两个用于其余两个节点的冗余，即一个主副本一个备份副本）
 * 5、
 *
 * 3副本情况：
 * 3个节点>>>>>>[[node1, node2, node3]]
 * 4个节点>>>>>>[[node1, node2, node3], [node1, node2, node4], [node1, node3, node4]]
 * 6个节点>>>>>>[[node1, node2, node3], [node2, node4, node5], [node1, node3, node6], [node4, node5, node6]]
 * 在新增节点时，先进行任务的预分配，根据任务id区间进行预分配，然后创建新的scheduler（即还未启动），
 * 将重新分配的任务添加进去，此时新的任务已经分配，然后启动的同时将旧分配的scheduler进行删除回收，此时完成新增节点的任务迁移工作。
 *
 * 如果是删除节点的话，不需要进行迁移工作，只需要把对应copyset中的节点重新定位到主节点即可。
 * 比如4个节点的情况，，node4挂掉，此时copyset2可以在node1和node2继续执行任务，copyset3可以在node1和node3继续执行任务，此时不需要进行重新迁移。
 * 只是需要考虑尽可能一个节点执行一个scheduler实例，避免多个scheduler实例在同一个节点执行，除非节点不够了。
 *
 * 任务状态： 提交中、队列中、运行中、运行成功、运行失败、重试成功、重试失败
 * https://baijiahao.baidu.com/s?id=1663391747827568685&wfr=spider&for=pc
 * https://developer.aliyun.com/article/709946
 * https://doc.akka.io/docs/akka/current/index.html?spm=a2c6h.12873639.0.0.2d7e3458B8hMEF&language=java
 * https://cn.aliyun.com/aliware/schedulerx
 * https://help.aliyun.com/document_detail/148187.html?spm=a2c4g.11186623.6.544.3e6a158azuuB0R
 *
 * @author tangzy
 * @since 1.0
 */
@Slf4j
public class ClusterNode {

  private final int processors = Runtime.getRuntime().availableProcessors();

  List<Node> nodeList = Lists.newArrayList(new Node("127.0.0.1:8080", "node1", "127.0.0.1", 8080, 0),
      new Node("127.0.0.1:8081", "node2", "127.0.0.1", 8081, 0),
      new Node("127.0.0.1:8082", "node3", "127.0.0.1", 8082, 0));

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
    int nums = getNodeListSize();
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
  private int getNodeListSize() {
    return nodeList.size();
  }

  private List<Node> getNodeList() {
    return nodeList;
  }

  private List<String> getNodeIds() {
    return nodeList.stream().map(Node::getId).collect(Collectors.toList());
  }

  private List<JobInfo> getJobInfoList() {
    return jobInfoList;
  }

  private int getJobSize() {
    return 0;
  }

  private List<List<String>> deliverCopySets(List<String> nodeIds, int replicaNum, int scatter) {
    List<List<String>> copySets = CopySets.buildCopySets(nodeIds, replicaNum, scatter);
    return copySets;
  }

  private void deliverJob() {
    int jobSize = getJobSize();
    if (jobSize <= 0) {
      return;
    }
    int nodeSize = getNodeListSize();

  }

  public void init() {
    /*
      这里分为节点选举，和scheduler选举。
      节点选举用于选择出master来分配copysets的落点。
      scheduler选举，用于在对应的copyset中选举出唯一的可以调度的节点。
      master节点负责分配copy sets，然后将请求转发给相应的节点处理。
      节点接受到了请求之后进行scheduler的分配，和scheduler之间的选举。
      master节点可以权重设置的相比其他节点小一点。
     */
    // 判断节点是否满足要求
    while (getNodeListSize() < 3) {
      log.warn("waiting node join cluster.");
      sleep(1000);
    }

    // 获取copy sets分布
    List<List<String>> copySets = deliverCopySets(getNodeIds(), 3, 2);
    log.info("copysets >>> {}", copySets);
    // 获取任务数量进行copySets分配
    int jobSize = getJobSize();
    if (jobSize <= 0) {
      return;
    }
    int unitSize = jobSize / copySets.size();
    int segment = (int) Math.ceil((double) jobSize / unitSize);
    //0 * segment, segment
    //1 * segment, segment
    //index * segment, segment

  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      // ignore
    }
  }

}
