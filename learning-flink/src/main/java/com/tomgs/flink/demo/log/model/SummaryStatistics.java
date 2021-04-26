package com.tomgs.flink.demo.log.model;

/**
 * 聚合统计
 *
 * @author tomgs
 * @since 2021/4/26
 */
public class SummaryStatistics {

  private int sum;

  private int count;

  private int max = Integer.MIN_VALUE;

  private int min = Integer.MAX_VALUE;

  public SummaryStatistics() {

  }

  public void accept(int value) {
    ++count;
    sum += value;
    min = Math.min(min, value);
    max = Math.max(max, value);
  }

  public int getSum() {
    return sum;
  }

  public int getCount() {
    return count;
  }

  public int getMax() {
    return max;
  }

  public int getMin() {
    return min;
  }

  public Double getAvg() {
    return getCount() > 0 ? (double) getSum() / getCount() : 0.0d;
  }

  @Override
  public String toString() {
    return String.format(
        "%s{count=%d, sum=%d, min=%d, average=%f, max=%d}",
        this.getClass().getSimpleName(),
        getCount(),
        getSum(),
        getMin(),
        getAvg(),
        getMax());
  }

}
