package com.tomgs.aviator.demo;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map;

/**
 * @author tomgs
 * @since 2021/4/26
 */
public class MCountFunction extends AbstractFunction {

  private IntSummaryStatistics statistics;

  public MCountFunction(IntSummaryStatistics statistics) {
    this.statistics = statistics;
  }

  @Override
  public String getName() {
    return "mcount";
  }

  @Override
  public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
    AviatorJavaType aviatorType = (AviatorJavaType) arg1;
    String name = aviatorType.getName();

    Map<String, Object> count = new HashMap<>();
    count.put("key", name);
    count.put("op", "count");

    // 这里可以根据业务去获取值信息，比如从IntSummaryStatistics中获取count
    long countValue = statistics.getCount();

    return AviatorRuntimeJavaType.valueOf(countValue);
  }
}
