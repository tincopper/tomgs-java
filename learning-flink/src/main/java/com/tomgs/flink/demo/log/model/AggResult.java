package com.tomgs.flink.demo.log.model;

import lombok.Data;

/**
 * @author tomgs
 * @since 2021/4/23
 */
@Data
public class AggResult {

  private int sum;

  private int count;

  private int max;

  private int min;

}
