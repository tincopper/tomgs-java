package com.tomgs.registry.jraft.demo.kv;

import com.tomgs.registry.jraft.demo.kv.core.RHeaKVClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * RHeaKVTest
 *
 * @author tomgs
 * @since 2021/9/26
 */
public class RHeaKVClientTest {

    private CacheClient<String, String> client;

    @Before
    public void setup() {
        Map<String, Object> rawConfigMap = new HashMap<>();
        rawConfigMap.put("ALL_NODE_ADDRESSES", "127.0.0.1:8181");
        rawConfigMap.put("CLUSTER_NAME", "test_cluster");

        CacheSourceConfig config = new CacheSourceConfig();
        config.setCacheRawConfig(rawConfigMap);
        client = new RHeaKVClient<>(config);
    }

    @Test
    public void testPutAndGet() {
        client.put("a", "b");
        String actualResult = client.get("a");
        System.out.println(actualResult);
        Assert.assertEquals("b", actualResult);
    }

}
