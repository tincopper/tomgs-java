package com.sentinel.extension.demo;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.ClientConstants;
import com.alibaba.csp.sentinel.cluster.client.TokenClientProvider;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.cluster.flow.statistic.ClusterMetricNodeGenerator;
import com.alibaba.csp.sentinel.cluster.flow.statistic.limit.GlobalRequestLimiter;
import com.alibaba.csp.sentinel.cluster.server.EmbeddedClusterTokenServerProvider;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.cluster.server.connection.ConnectionGroup;
import com.alibaba.csp.sentinel.cluster.server.connection.ConnectionManager;
import com.alibaba.csp.sentinel.command.entity.ClusterClientStateEntity;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/25 1.0 
 */
public class SentinelMonitor {

    public void start() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                fetchClusterServerInfo();
                fetchClusterMetricInfo();
                fetchClusterClientInfo();
                fetchClusterModeInfo();
            }
        }, 3, 2, TimeUnit.SECONDS);
    }

    private void fetchClusterModeInfo() {
        JSONObject res = new JSONObject()
                .fluentPut("mode", ClusterStateManager.getMode())
                .fluentPut("lastModified", ClusterStateManager.getLastModified())
                .fluentPut("clientAvailable", TokenClientProvider.getClient() != null)
                .fluentPut("serverAvailable", EmbeddedClusterTokenServerProvider.getServer() != null);
        System.out.println("CLusterModeInfo ### " + res.toJSONString());
    }

    private void fetchClusterClientInfo() {
        ClusterClientStateEntity stateVO = new ClusterClientStateEntity()
                .setServerHost(ClusterClientConfigManager.getServerHost())
                .setServerPort(ClusterClientConfigManager.getServerPort())
                .setRequestTimeout(ClusterClientConfigManager.getRequestTimeout());
        if (TokenClientProvider.isClientSpiAvailable()) {
            stateVO.setClientState(TokenClientProvider.getClient().getState());
        } else {
            stateVO.setClientState(ClientConstants.CLIENT_STATUS_OFF);
        }
        System.out.println("ClusterClientInfo ### " + JSON.toJSONString(stateVO));
    }

    private void fetchClusterMetricInfo() {
        String appName = AppNameUtil.getAppName();
        System.out.println("ClusterMetricInfo ### " + JSON.toJSONString(ClusterMetricNodeGenerator.generateCurrentNodeMap(appName)));
    }

    private void fetchClusterServerInfo() {
        JSONObject info = new JSONObject();
        JSONArray connectionGroups = new JSONArray();
        Set<String> namespaceSet = ClusterServerConfigManager.getNamespaceSet();
        for (String namespace : namespaceSet) {
            ConnectionGroup group = ConnectionManager.getOrCreateConnectionGroup(namespace);
            if (group != null) {
                connectionGroups.add(group);
            }
        }

        ServerTransportConfig transportConfig = new ServerTransportConfig()
                .setPort(ClusterServerConfigManager.getPort())
                .setIdleSeconds(ClusterServerConfigManager.getIdleSeconds());
        ServerFlowConfig flowConfig = new ServerFlowConfig()
                .setExceedCount(ClusterServerConfigManager.getExceedCount())
                .setMaxOccupyRatio(ClusterServerConfigManager.getMaxOccupyRatio())
                .setIntervalMs(ClusterServerConfigManager.getIntervalMs())
                .setSampleCount(ClusterServerConfigManager.getSampleCount())
                .setMaxAllowedQps(ClusterServerConfigManager.getMaxAllowedQps());

        JSONArray requestLimitData = buildRequestLimitData(namespaceSet);

        info.fluentPut("port", ClusterServerConfigManager.getPort())
                .fluentPut("connection", connectionGroups)
                .fluentPut("requestLimitData", requestLimitData)
                .fluentPut("transport", transportConfig)
                .fluentPut("flow", flowConfig)
                .fluentPut("namespaceSet", namespaceSet)
                .fluentPut("embedded", ClusterServerConfigManager.isEmbedded());

        // Since 1.5.0 the appName is carried so that the caller can identify the appName of the token server.
        info.put("appName", AppNameUtil.getAppName());

        System.out.println("ServerInfo ### " + info.toJSONString());
    }

    private JSONArray buildRequestLimitData(Set<String> namespaceSet) {
        JSONArray array = new JSONArray();
        for (String namespace : namespaceSet) {
            array.add(new JSONObject()
                    .fluentPut("namespace", namespace)
                    .fluentPut("currentQps", GlobalRequestLimiter.getCurrentQps(namespace))
                    .fluentPut("maxAllowedQps", GlobalRequestLimiter.getMaxAllowedQps(namespace))
            );
        }
        return array;
    }
}
