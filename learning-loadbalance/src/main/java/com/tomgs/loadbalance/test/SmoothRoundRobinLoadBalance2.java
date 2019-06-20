package com.tomgs.loadbalance.test;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 平滑加权轮询
 *
 * @author tangzhongyuan
 * @since 2019-04-22 14:22
 */
public class SmoothRoundRobinLoadBalance2 {

    protected ServerInfo doSelect(final List<ServerInfo> servers) {
        //对权重进行求和
        int total = 0;
        ServerInfo selectServer = null;
        for (ServerInfo server : servers) {
            final int weight = server.getWeight();
            final int currWeight = server.getCurWeight();
            server.setCurWeight(currWeight + weight);
            total += weight;

            if (selectServer == null || selectServer.getCurWeight() < server.getCurWeight()) {
                selectServer = server;
            }
        }
        for (ServerInfo server : servers) {
            if (server.equals(selectServer)) {
                final int currWeight = server.getCurWeight();
                server.setCurWeight(currWeight - total);
                break;
            }
        }
        return selectServer;
    }

    public static void main(String[] args) {
        SmoothRoundRobinLoadBalance2 loadBalance = new SmoothRoundRobinLoadBalance2();

        ServerInfo serverInfo1 = ServerInfo.builder().host("1").weight(5).build();
        ServerInfo serverInfo2 = ServerInfo.builder().host("2").weight(2).build();
        ServerInfo serverInfo3 = ServerInfo.builder().host("3").weight(3).build();
        ServerInfo serverInfo4 = ServerInfo.builder().host("4").weight(10).build();

        List<ServerInfo> servers = Lists.newArrayList(serverInfo1, serverInfo2, serverInfo3, serverInfo4);

        for (int i = 1; i <= 20; i++) {
            ServerInfo serverInfo = loadBalance.doSelect(servers);
            System.out.println(i + " select server is " + serverInfo.getHost());
        }
    }

}



