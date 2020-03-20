package com.tomgs.csp.sentinel.extension.common.init;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
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
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tomgs.csp.sentinel.extension.common.entity.ClusterGroupEntity;
import com.tomgs.csp.sentinel.extension.common.util.ZkConfigUtil;

import java.util.*;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/21 1.0 
 */
public class RuleInitManager {

    public static void initDynamicRuleProperty(String remoteAddress, String appName) {
        // 流控规则
        ReadableDataSource<String, List<FlowRule>> ruleSource = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getFlowDataId(appName), source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(ruleSource.getProperty());

        // 热点参数限流规则
        ReadableDataSource<String, List<ParamFlowRule>> paramRuleSource = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getParamFlowDataId(appName), source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
        ParamFlowRuleManager.register2Property(paramRuleSource.getProperty());

        // 降级规则
        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getDegradeDataId(appName), source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        // 系统规则
        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getSystemDataId(appName), source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

        // 授权规则
        ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getAuthorityDataId(appName), source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {}));
        AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
    }

    public static void initClientConfigProperty(String remoteAddress, String appName) {
        ReadableDataSource<String, ClusterClientConfig> clientConfigDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterClientConfig(appName),
                source -> JSON.parseObject(source, new TypeReference<ClusterClientConfig>() {}));
        ClusterClientConfigManager.registerClientConfigProperty(clientConfigDs.getProperty());
    }

    public static void initClientServerAssignProperty(String remoteAddress, String appName) {
        // Cluster map format:
        // [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
        // machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterMapConfig(appName), source -> {List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {});
            return Optional.ofNullable(groupList)
                    .flatMap(RuleInitManager::extractClientAssignment)
                    .orElse(null);
        });
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }

    public static void registerClusterRuleSupplier(String remoteAddress, String appName) {
        // Register cluster flow rule property supplier which creates data source by namespace.
        // Flow rule dataId format: ${namespace}-flow-rules
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<FlowRule>> ds = new ZookeeperDataSource<>(remoteAddress,
                    ZkConfigUtil.getFlowDataId(namespace),
                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
            return ds.getProperty();
        });
        // Register cluster parameter flow rule property supplier which creates data source by namespace.
        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<ParamFlowRule>> ds = new ZookeeperDataSource<>(remoteAddress,
                    ZkConfigUtil.getParamFlowDataId(namespace),
                    source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
            return ds.getProperty();
        });
    }

    public static void initServerTransportConfigProperty(String remoteAddress, String appName) {
        ReadableDataSource<String, ServerTransportConfig> serverTransportDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterMapConfig(appName),
                source -> {List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {});
                    return Optional.ofNullable(groupList)
                            .flatMap(RuleInitManager::extractServerTransportConfig)
                            .orElse(null);
                });
        ClusterServerConfigManager.registerServerTransportProperty(serverTransportDs.getProperty());
    }

    public static void initTokenServerNameSpaces(String remoteAddress, String appName) {
        ReadableDataSource<String, Set<String>> namespaceDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterNameSpaceDataId(appName),
                source -> JSON.parseObject(source, new TypeReference<Set<String>>() {}));
        ClusterServerConfigManager.registerNamespaceSetProperty(namespaceDs.getProperty());
        // 默认将自己的appName加入到namespace set中
        ClusterServerConfigManager.loadServerNamespaceSet(Collections.singleton(appName));
    }

    public static void initTokenServerStartConfig(String remoteAddress, String appName) {
        // setting token server start configuration
        ReadableDataSource<String, ServerTransportConfig> serverTransportDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterServerTransportConfig(appName), source -> JSON.parseObject(source, new TypeReference<ServerTransportConfig>() {}));
        ClusterServerConfigManager.registerServerTransportProperty(serverTransportDs.getProperty());
    }

    public static void initStateProperty(String remoteAddress, String appName) {
        // Cluster map format:
        // [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
        // machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
        ReadableDataSource<String, Integer> clusterModeDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterMapConfig(appName),
                source -> {List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {});
                    return Optional.ofNullable(groupList)
                            .map(RuleInitManager::extractMode)
                            .orElse(ClusterStateManager.CLUSTER_NOT_STARTED);
                });
        ClusterStateManager.registerProperty(clusterModeDs.getProperty());
    }

    public static void initServerFlowConfig(String remoteAddress, String appName) {
        ReadableDataSource<String, ServerFlowConfig> serverFlowConfigDs = new ZookeeperDataSource<>(remoteAddress,
                ZkConfigUtil.getClusterMapConfig(appName),
                source -> {List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {});
                    return Optional.ofNullable(groupList)
                            .flatMap(RuleInitManager::extractServerFlowConfig)
                            .orElse(null);
                });
        ClusterServerConfigManager.registerGlobalServerFlowProperty(serverFlowConfigDs.getProperty());
    }

    private static Optional<ServerFlowConfig> extractServerFlowConfig(List<ClusterGroupEntity> groupList) {
        return groupList.stream()
                //.filter(RuleInitManager::machineEqual)
                .findAny()
                .map(e -> new ServerFlowConfig()
                        .setExceedCount(ClusterServerConfigManager.getExceedCount())
                        .setIntervalMs(ClusterServerConfigManager.getIntervalMs())
                        .setMaxAllowedQps(e.getMaxAllowedQps())
                        .setMaxOccupyRatio(ClusterServerConfigManager.getMaxOccupyRatio())
                        .setSampleCount(ClusterServerConfigManager.getSampleCount()));
    }

    private static int extractMode(List<ClusterGroupEntity> groupList) {
        // If any server group machineId matches current, then it's token server.
        if (groupList.stream().anyMatch(RuleInitManager::machineEqual)) {
            return ClusterStateManager.CLUSTER_SERVER;
        }
        // If current machine belongs to any of the token server group, then it's token client.
        // Otherwise it's unassigned, should be set to NOT_STARTED.
        boolean canBeClient = groupList.stream()
                .flatMap(e -> e.getClientSet().stream())
                .filter(Objects::nonNull)
                .anyMatch(e -> e.equals(getCurrentMachineId()));
        return canBeClient ? ClusterStateManager.CLUSTER_CLIENT : ClusterStateManager.CLUSTER_NOT_STARTED;
    }

    private static Optional<ServerTransportConfig> extractServerTransportConfig(List<ClusterGroupEntity> groupList) {
        return groupList.stream()
                //.filter(RuleInitManager::machineEqual)
                .findAny()
                .map(e -> new ServerTransportConfig().setPort(e.getPort()).setIdleSeconds(600));
    }

    private static Optional<ClusterClientAssignConfig> extractClientAssignment(List<ClusterGroupEntity> groupList) {
        if (groupList.stream().anyMatch(RuleInitManager::machineEqual)) {
            return Optional.empty();
        }
        // Build client assign config from the client set of target server group.
        for (ClusterGroupEntity group : groupList) {
            if (group.getClientSet().contains(getCurrentMachineId())) {
                String ip = group.getIp();
                Integer port = group.getPort();
                return Optional.of(new ClusterClientAssignConfig(ip, port));
            }
        }
        return Optional.empty();
    }

    private static boolean machineEqual(/*@Valid*/ ClusterGroupEntity group) {
        return getCurrentMachineId().equals(group.getMachineId());
    }

    private static String getCurrentMachineId() {
        // Note: this may not work well for container-based env.
        return HostNameUtil.getIp() + SEPARATOR + TransportConfig.getRuntimePort();
    }

    private static final String SEPARATOR = "@";

}
