package com.tomgs.aviator.demo;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tomgs
 * @since 2021/4/26
 */
public class MCountFunction extends AbstractFunction {

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
    return AviatorRuntimeJavaType.valueOf(count);
  }
}
