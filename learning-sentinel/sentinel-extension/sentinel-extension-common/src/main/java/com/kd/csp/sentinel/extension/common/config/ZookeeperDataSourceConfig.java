package com.kd.csp.sentinel.extension.common.config;

import com.alibaba.csp.sentinel.util.StringUtil;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/20 1.0 
 */
public class ZookeeperDataSourceConfig {

    private static volatile String remoteAddress;

    public static String getRemoteAddress() {
        String result = StringUtil.isBlank(remoteAddress) ? System.getProperty("sentinel.datasource.address") : remoteAddress;
        if (StringUtil.isBlank(result)) {
            throw new IllegalArgumentException("zookeeper remote address must be not null");
        }
        return result;
    }

    public static void setRemoteAddress(String remoteAddress) {
        ZookeeperDataSourceConfig.remoteAddress = remoteAddress;
    }

}
