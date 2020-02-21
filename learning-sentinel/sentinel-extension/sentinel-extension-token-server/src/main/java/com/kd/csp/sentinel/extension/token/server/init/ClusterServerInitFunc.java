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
package com.kd.csp.sentinel.extension.token.server.init;

import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.kd.csp.sentinel.extension.common.config.ZookeeperDataSourceConfig;
import com.kd.csp.sentinel.extension.common.init.RuleInitManager;

/**
 * @author Eric Zhao
 */
public class ClusterServerInitFunc implements InitFunc {

    private static final String REMOTE_ADDRESS = ZookeeperDataSourceConfig.getRemoteAddress();
    private static final String APP_NAME = AppNameUtil.getAppName();

    @Override
    public void init() throws Exception {
        // Register cluster flow rule property supplier which creates data source by namespace.
        // Register cluster parameter flow rule property supplier.
        RuleInitManager.registerClusterRuleSupplier(REMOTE_ADDRESS, APP_NAME);
        // Server transport configuration data source.
        RuleInitManager.initServerTransportConfigProperty(REMOTE_ADDRESS, APP_NAME);
        // Server flow configuration data source.
        RuleInitManager.initServerFlowConfig(REMOTE_ADDRESS, APP_NAME);
        // Server namespace set (scope) data source.
       RuleInitManager.initTokenServerNameSpaces(REMOTE_ADDRESS, APP_NAME);
    }

}
