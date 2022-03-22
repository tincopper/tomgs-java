package com.tomgs.ratis.kv;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.HodorCacheSource;

/**
 * RatisKVDataSource
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisKVDataSource implements HodorCacheSource {

    @Override
    public String getCacheType() {
        return "ratiskv";
    }

    @Override
    public <K, V> CacheClient<K, V> getCacheClient(String group) {
        return null;
    }

    @Override
    public <K, V> CacheClient<K, V> getCacheClient() {
        return null;
    }

}
