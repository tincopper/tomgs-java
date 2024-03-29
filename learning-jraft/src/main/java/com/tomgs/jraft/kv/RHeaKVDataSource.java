package com.tomgs.jraft.kv;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.HodorCacheSource;

/**
 * RHeaKVDataSource
 *
 * @author tomgs
 * @since 2021/9/26
 */
public class RHeaKVDataSource implements HodorCacheSource {

    @Override
    public String getCacheType() {
        return "rheakv";
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
