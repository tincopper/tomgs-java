package com.tomgs.scheduler.quartz.customer.node;

import lombok.Data;

/**
 * @author tangzy
 * @since 1.0
 */
@Data
public class NodeMaping {

  private Node node;

  // 定义长度为2的数组，定义任务的起始位置和结束位置
  private Integer[] offset;

}
