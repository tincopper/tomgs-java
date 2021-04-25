package com.tomgs.aviator.demo;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import java.util.Map;

/**
 * add function
 *
 * @author tomgs
 * @since 2021/4/25
 */
public class AddFunction extends AbstractFunction {

  @Override
  public String getName() {
    return "add";
  }

  @Override
  public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
    Number left = FunctionUtils.getNumberValue(arg1, env);
    Number right = FunctionUtils.getNumberValue(arg2, env);
    return new AviatorDouble(left.doubleValue() + right.doubleValue());
  }

}
