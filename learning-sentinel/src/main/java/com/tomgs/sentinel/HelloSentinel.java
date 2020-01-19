package com.tomgs.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tangzy
 */
public class HelloSentinel {

  /*static {
    // 初始化控制台
    InitExecutor.doInit();
  }*/

  public static void main(String[] args) {
    // 初始化控制台
    InitExecutor.doInit();
    //initFlowRules();
    while (true) {
      Entry entry = null;
      try {
         entry = SphU.entry("HelloWorld");
        /*您的业务逻辑 - 开始*/
        System.out.println("hello world");
        TimeUnit.MILLISECONDS.sleep(500);
        /*您的业务逻辑 - 结束*/
      } catch (BlockException e) {
        /*流控逻辑处理 - 开始*/
        System.out.println("block!");
        /*流控逻辑处理 - 结束*/
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        if (entry != null) {
          entry.exit();
        }
      }
    }
  }

  private static void initFlowRules() {
    List<FlowRule> rules = new ArrayList<>();
    FlowRule rule = new FlowRule();
    rule.setResource("HelloWorld");
    rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
    // Set limit QPS to 20.
    rule.setCount(20);
    rules.add(rule);
    FlowRuleManager.loadRules(rules);
  }

}
