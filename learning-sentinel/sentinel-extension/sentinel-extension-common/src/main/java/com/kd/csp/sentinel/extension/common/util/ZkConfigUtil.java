package com.kd.csp.sentinel.extension.common.util;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/11 1.0 
 */
public class ZkConfigUtil {

    private static final String FLOW_RULES = "sentinel.flow.rules.properties";
    private static final String DEGRADE_RULES = "sentinel.degrade.rules.properties";
    private static final String AUTHORITY_RULES = "sentinel.authority.rules.properties";
    private static final String PARAM_FLOW_RULES = "sentinel.paramflow.rules.properties";
    private static final String SYSTEM_RULES = "sentinel.system.rules.properties";
    private static final String CLUSTER_CLIENT_CONFIG = "sentinel.cluster.client.config";
    private static final String CLUSTER_MAP_CONFIG = "sentinel.cluster.map.config";
    private static final String CLUSTER_NAMESPACE = "sentinel.cluster.namespace.set";

    public static String getGroupId(String appName) {
        return appName + "/config/common/prop";
    }

    public static String getFlowDataId(String appName) {
        return String.format("/%s/%s", getGroupId(appName), FLOW_RULES);
    }

    public static String getDegradeDataId(String appName) {
        return String.format("/%s/%s", getGroupId(appName), DEGRADE_RULES);
    }

    public static String getAuthorityDataId(String appName) {
        return String.format("/%s/%s", getGroupId(appName), AUTHORITY_RULES);
    }

    public static String getParamFlowDataId(String appName) {
        return String.format("/%s/%s", getGroupId(appName), PARAM_FLOW_RULES);
    }

    public static String getSystemDataId(String appName) {
        return String.format("/%s/%s", getGroupId(appName), SYSTEM_RULES);
    }

    public static String getClusterClientConfig(String appName) {
        return String.format("/%s/%s", getGroupId(appName), CLUSTER_CLIENT_CONFIG);
    }

    public static String getClusterMapConfig(String appName) {
        return String.format("/%s/%s", getGroupId(appName), CLUSTER_MAP_CONFIG);
    }

    public static String getClusterNameSpaceDataId(String appName) {
        return String.format("/%s/%s", getGroupId(appName), CLUSTER_NAMESPACE);
    }

}
