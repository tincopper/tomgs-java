package com.tomgs.ratis.customrpc.watchkv;

import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.ratis.kv.core.RatisKVClient;
import org.junit.Before;
import org.junit.Test;

/**
 * RatisKVServerTest
 *     // ratis client的操作可以学习ozone的XceiverClientRatis类
 *     // org.apache.hadoop.hdds.scm.XceiverClientRatis
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisWatchKVOpTest {

    private RatisKVClient<String, String> ratisKVClient;

    @Before
    public void before() {
        CacheSourceConfig sourceConfig = new CacheSourceConfig();
        sourceConfig.setTimeout(3000);
        sourceConfig.setServerAddresses("127.0.0.1:8001,127.0.0.1:8002,127.0.0.1:8003");
        //sourceConfig.setServerAddresses("127.0.0.1:8004");
        sourceConfig.setKeySederClass(String.class);
        sourceConfig.setValueSederClass(String.class);

        this.ratisKVClient = new RatisKVClient<>(sourceConfig);
    }

    @Test
    public void testPut() {
        System.out.println("=================PUT==================");
        ratisKVClient.put("hello", "world3");
    }

    @Test
    public void testGet() {
        System.out.println("=================GET==================");
        final String result = ratisKVClient.get("hello");
        System.out.println(result);
    }

    @Test
    public void testDelete() {
        System.out.println("=================DELETE==================");
        ratisKVClient.delete("hello");
    }

    @Test
    public void testUnwatch() {
        System.out.println("=================UNWATCH==================");
        ratisKVClient.unwatch("hello");
    }

}
