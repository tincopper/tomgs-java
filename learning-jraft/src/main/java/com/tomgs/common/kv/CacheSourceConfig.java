package com.tomgs.common.kv;

import lombok.Data;

import java.util.Map;

/**
 * cache source config
 *
 * @author tomgs
 * @since 2021/8/16
 */
@Data
public class CacheSourceConfig {

    private String clusterName;

    private String endpoint;

    private String serverAddresses;
    
    private String dataPath;

    private String dbPath;

    private int maximumSize;

    private Map<String, Object> cacheRawConfig;

}
