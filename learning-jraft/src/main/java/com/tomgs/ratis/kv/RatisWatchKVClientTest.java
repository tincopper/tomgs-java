package com.tomgs.ratis.kv;

import com.alipay.remoting.exception.CodecException;
import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.ratis.kv.core.ProtostuffSerializer;
import com.tomgs.ratis.kv.core.RatisKVDataSource;
import com.tomgs.ratis.kv.core.RatisVKClient;
import com.tomgs.ratis.kv.watch.DataChangeEvent;
import com.tomgs.ratis.kv.watch.DataChangeListener;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * RatisKVServerTest
 *
 *     // ratis client的操作可以学习ozone的XceiverClientRatis类
 *     // org.apache.hadoop.hdds.scm.XceiverClientRatis
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisWatchKVClientTest {

    public static void main(String[] args) throws InterruptedException, CodecException {
        CacheSourceConfig sourceConfig = new CacheSourceConfig();
        sourceConfig.setTimeout(3000);
        //sourceConfig.setServerAddresses("127.0.0.1:8001,127.0.0.1:8002,127.0.0.1:8003");
        sourceConfig.setServerAddresses("127.0.0.1:8004");
        sourceConfig.setKeySederClass(String.class);
        sourceConfig.setValueSederClass(String.class);

        RatisKVDataSource dataSource = new RatisKVDataSource(sourceConfig);
        RatisVKClient<String, String> cacheClient = (RatisVKClient) dataSource.getCacheClient();

        cacheClient.watch("hello", new DataChangeListener() {
            @Override
            public void dataChanged(DataChangeEvent event) {
                System.out.println("DataChangeEvent: " + event.getType() + ", " + event.getPath() + ", " + event.getData());
            }
        });

        Thread.sleep(1000);

        cacheClient.put("hello", "world");
        cacheClient.put("hello", "world1");
        cacheClient.delete("hello");
        cacheClient.put("hello", "world2");

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();
    }

}
