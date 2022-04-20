package com.tomgs.common.kv;

import com.tomgs.ratis.kv.watch.DataChangeListener;

/**
 * cache client
 *
 * @author tomgs
 * @since 2021/8/11
 */
public interface CacheClient<K, V> {

    V get(K key);

    void put(K key, V value);

    void put(K key, V value, int expire);

    void delete(K key);

    void clear();

    void close();

    void watch(K key, DataChangeListener dataChangeListener);
}
