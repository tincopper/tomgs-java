/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.sentinel.cluster.server.init;

import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tomgs.sentinel.cluster.server.entity.ClusterGroupEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Eric Zhao
 */
public class DemoClusterServerInitFunc implements InitFunc {

    private final String remoteAddress = "localhost";
    private final String groupId = "token-server";
    private final String namespaceSetDataId = "cluster-server-namespace-set";
    private final String serverTransportDataId = "cluster-server-transport-config";

    @Override
    public void init() throws Exception {
        // Register cluster flow rule property supplier which creates data source by namespace.
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            String path = "/" + namespace + "/config/common/prop/sentinel.flow.rules.properties";
            ReadableDataSource<String, List<FlowRule>> ds = new ZookeeperDataSource<>(remoteAddress, path,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
            return ds.getProperty();
        });
        // Register cluster parameter flow rule property supplier.
        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            String path = "/" + namespace + "/config/common/prop/sentinel.paramflow.rules.properties";
            ReadableDataSource<String, List<ParamFlowRule>> ds = new ZookeeperDataSource<>(remoteAddress, path,
                source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
            return ds.getProperty();
        });
        // Server namespace set (scope) data source.
        ReadableDataSource<String, Set<String>> namespaceDs = new ZookeeperDataSource<>(remoteAddress, groupId,
            namespaceSetDataId, source -> JSON.parseObject(source, new TypeReference<Set<String>>() {}));
        ClusterServerConfigManager.registerNamespaceSetProperty(namespaceDs.getProperty());
        // Server transport configuration data source.
        ReadableDataSource<String, ServerTransportConfig> transportConfigDs = new ZookeeperDataSource<>(remoteAddress,
            groupId, serverTransportDataId,
            source -> JSON.parseObject(source, new TypeReference<ServerTransportConfig>() {}));
        ClusterServerConfigManager.registerServerTransportProperty(transportConfigDs.getProperty());
        // Server flow configuration data source.
        ReadableDataSource<String, ServerFlowConfig> serverFlowConfigDs = new ZookeeperDataSource<>(remoteAddress,
                groupId, serverTransportDataId,
                source -> {List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {});
                    return Optional.ofNullable(groupList)
                            .flatMap(this::extractServerFlowConfig)
                            .orElse(null);
                });
        ClusterServerConfigManager.registerGlobalServerFlowProperty(serverFlowConfigDs.getProperty());
    }

    private Optional<ServerFlowConfig> extractServerFlowConfig(List<ClusterGroupEntity> groupList) {
        return groupList.stream()
                .filter(this::machineEqual)
                .findAny()
                .map(e -> new ServerFlowConfig()
                        .setExceedCount(ClusterServerConfigManager.getExceedCount())
                        .setIntervalMs(ClusterServerConfigManager.getIntervalMs())
                        .setMaxAllowedQps(e.getMaxAllowedQps())
                        .setMaxOccupyRatio(ClusterServerConfigManager.getMaxOccupyRatio())
                        .setSampleCount(ClusterServerConfigManager.getSampleCount()));
    }

    private boolean machineEqual(/*@Valid*/ ClusterGroupEntity group) {
        return getCurrentMachineId().equals(group.getMachineId());
    }

    private String getCurrentMachineId() {
        // Note: this may not work well for container-based env.
        return HostNameUtil.getIp() + SEPARATOR + TransportConfig.getRuntimePort();
    }

    private static final String SEPARATOR = "@";

}
