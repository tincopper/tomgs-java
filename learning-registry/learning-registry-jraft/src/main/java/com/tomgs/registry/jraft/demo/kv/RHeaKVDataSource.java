package com.tomgs.registry.jraft.demo.kv;

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
