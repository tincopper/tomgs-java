package com.tomgs.registry.jraft.demo.kv;

import com.tomgs.registry.jraft.demo.kv.core.RHeaKVServer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * RHeaKVServerTest
 *
 * @author tomgs
 * @since 2021/9/26
 */
@Slf4j
public class RHeaKVServerTest {

    public static void main(String[] args) {
        Map<String, Object> rawConfigMap = new HashMap<>();
        rawConfigMap.put("ALL_NODE_ADDRESSES", "127.0.0.1:8181");
        rawConfigMap.put("CLUSTER_NAME", "test_cluster");
        rawConfigMap.put("RAFT_DATA_PATH", "raftDataPath/");
        rawConfigMap.put("DB_PATH", "raftDbPath/");

        CacheSourceConfig config = new CacheSourceConfig();
        config.setCacheRawConfig(rawConfigMap);
        RHeaKVServer server = new RHeaKVServer(config);
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }

}
