package com.tomgs.sentinel.spi;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
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

/**
 * 从apollo获取规则初始化
 *
 * @author tangzy
 * @since 1.0.0
 */
public class RuleApolloProviderInitFunc implements InitFunc {

  @Override
  public void init() throws Exception {
    String namespaceName = ApolloConfigUtil.getNamespaceName();
    String defaultFlowRules = "[]";
    // 流控规则
    ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(namespaceName,
        ApolloConfigUtil.getFlowDataId(), defaultFlowRules, source -> JSON
        .parseObject(source, new TypeReference<List<FlowRule>>() {
        }));
    FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

    // 降级规则
    ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ApolloDataSource<>(namespaceName,
        ApolloConfigUtil.getDegradeDataId(), defaultFlowRules, source -> JSON
        .parseObject(source, new TypeReference<List<DegradeRule>>() {
        }));
    DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

    // 热点规则，需要单独引用sentinel-parameter-flow-control jar包
    ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new ApolloDataSource<>(namespaceName,
        ApolloConfigUtil.getParamFlowDataId(), defaultFlowRules, source -> JSON
        .parseObject(source, new TypeReference<List<ParamFlowRule>>() {
        }));
    ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());

    // 系统规则
    ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ApolloDataSource<>(namespaceName,
        ApolloConfigUtil.getSystemDataId(), defaultFlowRules, source -> JSON
        .parseObject(source, new TypeReference<List<SystemRule>>() {
        }));
    SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

    // 授权规则
    ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = new ApolloDataSource<>(namespaceName,
        ApolloConfigUtil.getAuthorityDataId(), defaultFlowRules, source -> JSON
        .parseObject(source, new TypeReference<List<AuthorityRule>>() {
        }));
    AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
  }

}
