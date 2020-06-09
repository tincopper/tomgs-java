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
public class Node {

  private String id;

  private String name;

  private String ip;

  private Integer port;

}
