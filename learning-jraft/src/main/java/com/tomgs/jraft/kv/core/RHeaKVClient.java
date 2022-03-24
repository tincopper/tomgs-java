package com.tomgs.jraft.kv.core;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RegionRouteTableOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.configured.MultiRegionRouteTableOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;
import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * RHeaKVClient
 *
 * @author tomgs
 * @since 2021/9/26
 */
@Slf4j
public class RHeaKVClient<K, V> implements CacheClient<K, V> {

    private final RheaKVStore rheaKVStore = new DefaultRheaKVStore();

    public RHeaKVClient(CacheSourceConfig cacheSourceConfig) {
        Map<String, Object> rawConfig = cacheSourceConfig.getCacheRawConfig();
        final List<RegionRouteTableOptions> regionRouteTableOptionsList = MultiRegionRouteTableOptionsConfigured
                .newConfigured() //
                .withInitialServerList(-1L /* default id */, (String) rawConfig.get("ALL_NODE_ADDRESSES"))//
                .config();
        final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured.newConfigured() //
                .withFake(true) //
                .withRegionRouteTableOptionsList(regionRouteTableOptionsList) //
                .config();
        final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured() //
                .withClusterName((String) rawConfig.get("CLUSTER_NAME")) //
                .withPlacementDriverOptions(pdOpts) //
                .config();

        log.info("RHeaKVClient store options: {}.", opts);

        rheaKVStore.init(opts);
    }

    private final TypeReference<V> typeReference = new TypeReference<V>() {};

    @Override
    public V get(K key) {
        byte[] keyBytes = JSONUtil.toJsonStr(key).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = rheaKVStore.bGet(keyBytes);
        String valueString = new String(valueBytes, StandardCharsets.UTF_8);
        if (JSONUtil.isJson(valueString)) {
            return JSONUtil.toBean(valueString, typeReference.getType(), true);
        }
        return (V) StrUtil.str(valueBytes, StandardCharsets.UTF_8);
    }

    @Override
    public void put(K key, V value) {
        byte[] keyBytes = JSONUtil.toJsonStr(key).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = JSONUtil.toJsonStr(value).getBytes(StandardCharsets.UTF_8);
        rheaKVStore.bPut(keyBytes, valueBytes);
    }

    @Override
    public void put(K key, V value, int expire) {
        put(key, value);
    }

    @Override
    public void delete(K key) {
        byte[] keyBytes = JSONUtil.toJsonStr(key).getBytes(StandardCharsets.UTF_8);
        rheaKVStore.delete(keyBytes);
    }

    @Override
    public void clear() {
        rheaKVStore.shutdown();
    }

    @Override
    public void close() {
        clear();
    }

}
