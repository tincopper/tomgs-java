package com.tomgs.jraft.kv.core;

import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.StoreEngineOptions;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RocksDBOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.StoreEngineOptionsConfigured;
import com.alipay.sofa.jraft.rhea.storage.StorageType;
import com.alipay.sofa.jraft.util.Endpoint;
import com.tomgs.common.kv.CacheSourceConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * RHeaKVServer
 *
 * @author tomgs
 * @since 2021/9/26
 */
@Slf4j
public class RHeaKVServer {

    private final CacheSourceConfig config;

    private Node node;

    public RHeaKVServer(final CacheSourceConfig config) {
        this.config = config;
    }

    public void start() {
        Map<String, Object> rawConfig = config.getCacheRawConfig();
        final PlacementDriverOptions pdOpts = PlacementDriverOptionsConfigured.newConfigured()
                .withFake(true) // use a fake pd
                .config();
        final StoreEngineOptions storeOpts = StoreEngineOptionsConfigured.newConfigured() //
                .withStorageType(StorageType.RocksDB)
                .withRocksDBOptions(RocksDBOptionsConfigured.newConfigured().withDbPath(String.valueOf(rawConfig.get("DB_PATH"))).config())
                .withRaftDataPath(String.valueOf(rawConfig.get("RAFT_DATA_PATH")))
                .withServerAddress(new Endpoint("127.0.0.1", 8181))
                .config();
        final RheaKVStoreOptions opts = RheaKVStoreOptionsConfigured.newConfigured() //
                .withClusterName((String) rawConfig.get("CLUSTER_NAME")) //
                .withInitialServerList((String) rawConfig.get("ALL_NODE_ADDRESSES"))
                .withStoreEngineOptions(storeOpts) //
                .withPlacementDriverOptions(pdOpts) //
                .config();

        log.info("RHeaKVServer store options {}.", opts);

        node = new Node(opts);
        node.start();
    }

    public void stop() {
        node.stop();
    }

}
