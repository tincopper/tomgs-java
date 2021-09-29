package com.tomgs.jraft.kv;

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

    private int maximumSize;

    private Map<String, Object> cacheRawConfig;

}
