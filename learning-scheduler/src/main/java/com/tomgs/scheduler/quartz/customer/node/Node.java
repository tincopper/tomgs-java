package com.tomgs.scheduler.quartz.customer.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangzy
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node implements Comparable<Node> {

  private String id;

  private String name;

  private String ip;

  private Integer port;

  // 权重，用于节点之间的排序，此权重用在节点转移copyset的优先级选择
  private Integer weight;

  @Override
  public int compareTo(Node o) {
    return this.getWeight() - o.getWeight();
  }

}
