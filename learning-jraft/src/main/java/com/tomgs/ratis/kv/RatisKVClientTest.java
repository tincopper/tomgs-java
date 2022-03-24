package com.tomgs.ratis.kv;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.ratis.kv.core.RatisKVDataSource;
import org.junit.Before;
import org.junit.Test;

/**
 * RatisKVServerTest
 *
 *     // ratis client的操作可以学习ozone的XceiverClientRatis类
 *     // org.apache.hadoop.hdds.scm.XceiverClientRatis
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisKVClientTest {
    private CacheClient<String, String> cacheClient;

    @Before
    public void before() {
        CacheSourceConfig sourceConfig = new CacheSourceConfig();
        sourceConfig.setTimeout(3000);
        sourceConfig.setServerAddresses("127.0.0.1:8001,127.0.0.1:8002,127.0.0.1:8003");

        RatisKVDataSource dataSource = new RatisKVDataSource(sourceConfig);
        this.cacheClient = dataSource.getCacheClient();
    }

    @Test
    public void testPut() {
        System.out.println("=================PUT==================");
        cacheClient.put("hello", "world");
    }

    @Test
    public void testGet() {
        System.out.println("=================GET==================");
        final String result = cacheClient.get("hello");
        System.out.println(result);
    }

    @Test
    public void testDelete() {
        System.out.println("=================DELETE==================");
        cacheClient.delete("hello");
    }

}
