package com.tomgs.learning.logger.log4j;

import java.util.Arrays;

/**
 * @author tomgs
 * @since 2021/2/19
 */
public class TestStackTrace {

  public static void main(String[] args) {
    StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
    System.out.println(Arrays.toString(stackTraceElement));

    for (StackTraceElement traceElement : stackTraceElement) {
      String className = traceElement.getClassName();
      String fileName = traceElement.getFileName();
      String methodName = traceElement.getMethodName();
      int lineNumber = traceElement.getLineNumber();

      System.out.println("类名：" + className);
      System.out.println("字段名：" + fileName);
      System.out.println("方法名：" + methodName);
      System.out.println("行号：" + lineNumber);
    }
  }

}
