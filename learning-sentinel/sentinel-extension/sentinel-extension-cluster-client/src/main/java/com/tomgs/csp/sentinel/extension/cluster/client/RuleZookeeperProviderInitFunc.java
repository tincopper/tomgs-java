package com.tomgs.csp.sentinel.extension.cluster.client;

import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.tomgs.csp.sentinel.extension.common.config.ZookeeperDataSourceConfig;
import com.tomgs.csp.sentinel.extension.common.init.RuleInitManager;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/17 1.0 
 */
public class RuleZookeeperProviderInitFunc implements InitFunc {

    private static final String APP_NAME = AppNameUtil.getAppName();
    private static final String REMOTE_ADDRESS = ZookeeperDataSourceConfig.getRemoteAddress();

    @Override
    public void init() throws Exception {
        RecordLog.info("RuleZookeeperProviderInitFunc ### start init...");
        //remoteAddress = ZookeeperDataSourceConfig.getRemoteAddress();
        // Register client dynamic rule data source.
        RuleInitManager.initDynamicRuleProperty(REMOTE_ADDRESS, APP_NAME);
        // Register token client related data source.
        // Token client common config:
        RuleInitManager.initClientConfigProperty(REMOTE_ADDRESS, APP_NAME);
        // Token client assign config (e.g. target token server) retrieved from assign map:
        RuleInitManager.initClientServerAssignProperty(REMOTE_ADDRESS, APP_NAME);
        // Register token server related data source.
        // Register dynamic rule data source supplier for token server:
        RuleInitManager.registerClusterRuleSupplier(REMOTE_ADDRESS, APP_NAME);
        // Token server transport config extracted from assign map:
        RuleInitManager.initServerTransportConfigProperty(REMOTE_ADDRESS, APP_NAME);
        // Init cluster state property for extracting mode from cluster map data source.
        RuleInitManager.initStateProperty(REMOTE_ADDRESS, APP_NAME);
        // 初始化最大qps
        RuleInitManager.initServerFlowConfig(REMOTE_ADDRESS, APP_NAME);
        // 初始化namespace
        RuleInitManager.initTokenServerNameSpaces(REMOTE_ADDRESS, APP_NAME);

        RecordLog.info("RuleZookeeperProviderInitFunc ### init success...");
    }

}
