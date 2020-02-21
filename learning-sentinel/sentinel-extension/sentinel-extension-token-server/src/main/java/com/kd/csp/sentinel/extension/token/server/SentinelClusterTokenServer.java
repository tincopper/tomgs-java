package com.kd.csp.sentinel.extension.token.server;

import com.alibaba.csp.sentinel.cluster.server.ClusterTokenServer;
import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/14 1.0 
 */
public class SentinelClusterTokenServer {

    public static void main(String[] args) throws Exception {
        // Not embedded mode by default (alone mode).
        ClusterTokenServer tokenServer = new SentinelDefaultTokenServer();

        // A sample for manually load config for cluster server.
        // It's recommended to use dynamic data source to cluster manage config and rules.
        // See the sample in DemoClusterServerInitFunc for detail.
        //ClusterServerConfigManager.loadGlobalTransportConfig(new ServerTransportConfig()
        //        .setIdleSeconds(600)
        //        .setPort(11111));
        //ClusterServerConfigManager.loadServerNamespaceSet(Collections.singleton(DemoConstants.APP_NAME));

        // Start the server.
        tokenServer.start();
    }
    
}
