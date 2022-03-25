package com.tomgs.ratis.kv.core;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.common.kv.HodorCacheSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RatisKVDataSource
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisKVDataSource implements HodorCacheSource {

    private final CacheSourceConfig cacheSourceConfig;

    private final Map<String, CacheClient<Object, Object>> groupCacheClientMap;

    public RatisKVDataSource(final CacheSourceConfig cacheSourceConfig) {
        this.cacheSourceConfig = cacheSourceConfig;
        this.groupCacheClientMap = new ConcurrentHashMap<>();
    }

    @Override
    public String getCacheType() {
        return "ratiskv";
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> CacheClient<K, V> getCacheClient(String group) {
        return (CacheClient<K, V>) groupCacheClientMap.computeIfAbsent(group, k -> new RatisVKClient<>(cacheSourceConfig));
    }

    @Override
    public <K, V> CacheClient<K, V> getCacheClient() {
        return getCacheClient("default");
    }

}
