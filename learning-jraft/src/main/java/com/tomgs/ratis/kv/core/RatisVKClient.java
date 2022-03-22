package com.tomgs.ratis.kv.core;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;

/**
 * RatisVKClient
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisVKClient<K, V> implements CacheClient<K, V> {

    private final CacheSourceConfig cacheSourceConfig;

    public RatisVKClient(final CacheSourceConfig cacheSourceConfig) {
        this.cacheSourceConfig = cacheSourceConfig;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void put(K key, V value, int expire) {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void close() {

    }

}
