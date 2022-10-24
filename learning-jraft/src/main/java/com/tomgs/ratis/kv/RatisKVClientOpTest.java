package com.tomgs.ratis.kv;

import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.common.ProtostuffSerializer;
import com.tomgs.ratis.kv.core.RatisKVDataSource;
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
public class RatisKVClientOpTest {

    private CacheClient<String, String> cacheClient;

    @Before
    public void before() {
        CacheSourceConfig sourceConfig = new CacheSourceConfig();
        sourceConfig.setTimeout(3000);
        sourceConfig.setServerAddresses("127.0.0.1:8001,127.0.0.1:8002,127.0.0.1:8003");
        //sourceConfig.setServerAddresses("127.0.0.1:8004");
        sourceConfig.setKeySederClass(String.class);
        sourceConfig.setValueSederClass(String.class);

        RatisKVDataSource dataSource = new RatisKVDataSource(sourceConfig);
        this.cacheClient = dataSource.getCacheClient();
        //this.cacheClient.setValueType(String.class);
    }

    @Test
    public void testPut() {
        System.out.println("=================PUT==================");
        cacheClient.put("hello", "world2");
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

    @Test
    public void testUnwatch() {
        System.out.println("=================UNWATCH==================");
        cacheClient.unwatch("hello");
    }

    @Test
    public void testSeder() {
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        final String hello = new String("hello");
        final byte[] serialize = serializer.serialize(hello);

        final Object deserialize = serializer.deserialize(serialize, String.class.getName());
        System.out.println(deserialize);

        ProtostuffSerializer serializer1 = new ProtostuffSerializer();
        final String hello1 = new String("hello");
        final byte[] serialize1 = serializer1.serialize(hello1);

        final Object deserialize1 = serializer1.deserialize(serialize1, String.class.getName());
        System.out.println(deserialize1);
    }

}
