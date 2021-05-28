package com.tomgs.aviator.demo;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import java.util.Map;

/**
 * contains function
 *
 * @author tomgs
 * @since 2021/5/19
 */
public class ContainsFunction extends AbstractFunction {

  @Override
  public String getName() {
    return "contains";
  }

  @Override
  public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
    AviatorJavaType aviatorType = (AviatorJavaType) arg1;
    String name = aviatorType.getName();

    boolean result = env.get(name) != null;

    return AviatorRuntimeJavaType.valueOf(result);
  }

}
