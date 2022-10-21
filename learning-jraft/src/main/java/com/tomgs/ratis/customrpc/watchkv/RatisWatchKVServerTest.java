package com.tomgs.ratis.customrpc.watchkv;

import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.ratis.customrpc.watchkv.core.RatisWatchKVServer;

import java.io.IOException;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * RatisKVServerTest
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisWatchKVServerTest {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java -cp *.jar com.tomgs.ratis.customrpc.watchkv.RatisWatchKVServerTest {endpoint} {serverList}");
            System.err.println("{endpoint} could be 127.0.0.1:8001");
            System.err.println("{serverList} could be 127.0.0.1:8001,127.0.0.1:8002,127.0.0.1:8003");
            System.exit(1);
        }

        CacheSourceConfig config = new CacheSourceConfig();
        config.setClusterName("test_cluster");
        config.setDataPath("./target/ratis_watchkv/data");
        config.setDbPath("./target/ratis_watchkv/db");
        config.setEndpoint(args[0]);
        config.setServerAddresses(args[1]);
        RatisWatchKVServer ratisWatchKVServer = new RatisWatchKVServer(config);
        ratisWatchKVServer.start();

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();
        ratisWatchKVServer.close();
    }

}
