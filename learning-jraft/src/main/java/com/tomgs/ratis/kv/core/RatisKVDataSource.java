package com.tomgs.ratis.kv.core;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.common.kv.HodorCacheSource;

/**
 * RatisKVDataSource
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisKVDataSource implements HodorCacheSource {

    private final RatisVKClient ratisVKClient;

    public RatisKVDataSource(final CacheSourceConfig cacheSourceConfig) {
        this.ratisVKClient = new RatisVKClient(cacheSourceConfig);
    }

    @Override
    public String getCacheType() {
        return "ratiskv";
    }

    @Override
    @SuppressWarnings("unchecked")
    public CacheClient<String, String> getCacheClient(String group) {
        return ratisVKClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CacheClient<String, String> getCacheClient() {
        return getCacheClient("default");
    }

}
