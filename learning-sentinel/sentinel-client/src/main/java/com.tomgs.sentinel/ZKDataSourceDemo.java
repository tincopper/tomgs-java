package com.tomgs.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tomgs.sentinel.spi.ApolloConfigUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=zk-datasource
 *
 * @author tomgs
 * @version 2020/2/10 1.0
 */
public class ZKDataSourceDemo {

    public static void main(String[] args) throws InterruptedException {
        InitExecutor.doInit();
        //loadRules();
        // Assume we config: resource is `TestResource`, initial QPS threshold is 5.
        while (true) {
            Entry entry = null;
            try {
                entry = SphU.entry("TestResource",EntryType.IN, 1, 1);
                /*您的业务逻辑 - 开始*/
                System.out.println("hello world");
                /*您的业务逻辑 - 结束*/
            } catch (BlockException e) {
                /*流控逻辑处理 - 开始*/
                System.out.println("block!");
                /*流控逻辑处理 - 结束*/
            } finally {
                if (entry != null) {
                    entry.exit(1, 1);
                }
            }

            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    private static void loadRules() {
        String appName = System.getProperty("project.name");
        String remoteAddress = "127.0.0.1:2181";
        String path = "/" + appName + "/config/common/prop/sentinel.flow.rules.properties";
        Converter<String, List<FlowRule>> parser = source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {});
        ReadableDataSource<String, List<FlowRule>> zookeeperDataSource = new ZookeeperDataSource<>(remoteAddress, path, parser);
        FlowRuleManager.register2Property(zookeeperDataSource.getProperty());

        String path1 = "/" + appName + "/config/common/prop/sentinel.degrade.rules.properties";
        Converter<String, List<DegradeRule>> parser1 = source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {});
        ReadableDataSource<String, List<DegradeRule>> zookeeperDataSource1 = new ZookeeperDataSource<>(remoteAddress, path1, parser1);
        DegradeRuleManager.register2Property(zookeeperDataSource1.getProperty());

        // 热点规则，需要单独引用sentinel-parameter-flow-control jar包
        String path2 = "/" + appName + "/config/common/prop/sentinel.paramflow.rules.properties";
        Converter<String, List<ParamFlowRule>> parser2 = source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {});

        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new ZookeeperDataSource<>(remoteAddress, path2, parser2);
        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());
    }

}
