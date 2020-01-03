package com.tomgs.core.base.manager;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

/**
 * @author tangzy
 */
public class DumpTest {

  public static void main(String[] args) {
    ThreadInfo[] threadInfos = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
    System.out.println(threadInfos);
  }

}
