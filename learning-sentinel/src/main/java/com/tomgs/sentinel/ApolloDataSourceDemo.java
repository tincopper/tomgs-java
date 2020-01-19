package com.tomgs.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This demo shows how to use Apollo as the data source of Sentinel rules.
 * <br />
 * You need to first set up data as follows:
 * <ol>
 *  <li>Create an application with app id as sentinel-demo in Apollo</li>
 *  <li>
 *    Create a configuration with key as flowRules and value as follows:
 *    <pre>
 *      [
          {
            "resource": "TestResource",
            "controlBehavior": 0,
            "count": 5.0,
            "grade": 1,
            "limitApp": "default",
            "strategy": 0
          }
        ]
 *    </pre>
 *  </li>
 *  <li>Publish the application namespace</li>
 * </ol>
 * Then you could start this demo and adjust the rule configuration as you wish.
 * The rule changes will take effect in real time.
 *
 * @author Jason Song
 */
public class ApolloDataSourceDemo {

    public static void main(String[] args) {
        InitExecutor.doInit();
        loadRules();
        // Assume we config: resource is `TestResource`, initial QPS threshold is 5.
        while (true) {
            Entry entry = null;
            try {
                entry = SphU.entry("TestResource");
                /*您的业务逻辑 - 开始*/
                System.out.println("hello world");
                TimeUnit.MILLISECONDS.sleep(1000);
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

    private static void loadRules() {
        // Set up basic information, only for demo purpose. You may adjust them based on your actual environment.
        // For more information, please refer https://github.com/ctripcorp/apollo
        String appId = "sentinel-demo";
        String apolloMetaServerAddress = "http://172.20.183.155:8080";
        String namespaceName = "application";
        String appName = System.getProperty("project.name");

        System.setProperty("app.id", appId);
        System.setProperty("apollo.meta", apolloMetaServerAddress);
        System.setProperty("env", "DEV");

        // It's better to provide a meaningful default value.
        String defaultFlowRules = "[]";

        // 流控规则
        String flowRuleKey = appName + "_flow-rules";
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(namespaceName,
            flowRuleKey, defaultFlowRules, source -> JSON
            .parseObject(source, new TypeReference<List<FlowRule>>() {
        }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        // 降级规则
        String degradeRuleKey = appName + "_degrade-rules";
        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ApolloDataSource<>(namespaceName,
            degradeRuleKey, defaultFlowRules, source -> JSON
            .parseObject(source, new TypeReference<List<DegradeRule>>() {
            }));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        // 热点规则，需要单独引用sentinel-parameter-flow-control jar包
        String paramFlowRuleKey = appName + "_param-flow-rules";
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new ApolloDataSource<>(namespaceName,
            paramFlowRuleKey, defaultFlowRules, source -> JSON
            .parseObject(source, new TypeReference<List<ParamFlowRule>>() {
            }));
        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());

        // 系统规则
        String systemRuleKey = appName + "_system-rules";
        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ApolloDataSource<>(namespaceName,
            systemRuleKey, defaultFlowRules, source -> JSON
            .parseObject(source, new TypeReference<List<SystemRule>>() {
            }));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

        // 授权规则
        String authorityRuleKey = appName + "_authority-rules";
        ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = new ApolloDataSource<>(namespaceName,
            authorityRuleKey, defaultFlowRules, source -> JSON
            .parseObject(source, new TypeReference<List<AuthorityRule>>() {
            }));
        AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
    }

}
