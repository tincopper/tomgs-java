package com.tomgs.ratis.kv;

import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.ratis.kv.core.ProtostuffSerializer;
import com.tomgs.ratis.kv.core.RatisKVDataSource;
import com.tomgs.ratis.kv.core.RatisVKClient;
import com.tomgs.ratis.kv.watch.DataChangeEvent;
import com.tomgs.ratis.kv.watch.DataChangeListener;

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

    public static void main(String[] args) throws InterruptedException {
        CacheSourceConfig sourceConfig = new CacheSourceConfig();
        sourceConfig.setTimeout(3000);
        sourceConfig.setServerAddresses("127.0.0.1:8001,127.0.0.1:8002,127.0.0.1:8003");
        //sourceConfig.setServerAddresses("127.0.0.1:8004");
        sourceConfig.setKeySederClass(String.class);
        sourceConfig.setValueSederClass(String.class);

        RatisKVDataSource dataSource = new RatisKVDataSource(sourceConfig);
        RatisVKClient<String, String> cacheClient = (RatisVKClient) dataSource.getCacheClient();

        cacheClient.watch("hello", new DataChangeListener() {
            @Override
            public void dataChanged(DataChangeEvent event) {
                System.out.println("DataChangeEvent: " + event.getType() + ", " + event.getPath() + ", "
                        + ProtostuffSerializer.INSTANCE.deserialize(event.getData(), String.class.getName()));
            }
        });

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();
    }

}
