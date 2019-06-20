package com.tomgs.loadbalance.test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 平滑加权轮询
 *
 * @author tangzhongyuan
 * @since 2019-04-22 14:22
 */
public class SmoothRoundRobinLoadBalance {

    private final Map<String, Integer> serverInfoIntegerMap = new ConcurrentHashMap<>();

    protected ServerInfo doSelect(final List<ServerInfo> servers) {
        //对权重进行求和
        int total = 0;
        ServerInfo selectServer = null;
        for (ServerInfo server : servers) {
            final String host = server.getHost();
            final int weight = server.getWeight();
            final int currWeight = serverInfoIntegerMap.get(host);
            serverInfoIntegerMap.put(host, currWeight + weight);
            total += weight;
            if (selectServer == null || serverInfoIntegerMap.get(selectServer.getHost())
                    < serverInfoIntegerMap.get(host)) {
                selectServer = server;
            }
        }
        for (ServerInfo server : servers) {
            if (server.equals(selectServer)) {
                final String host = server.getHost();
                final int currWeight = serverInfoIntegerMap.get(host);
                serverInfoIntegerMap.put(host, currWeight - total);
                break;
            }
        }
        return selectServer;
    }


}



