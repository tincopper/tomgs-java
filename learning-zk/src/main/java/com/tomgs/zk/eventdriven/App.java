package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException
    {
        Configuration cfg = new Configuration();
        cfg.setNamespace("/event-driven");
        cfg.setZkAddrs("127.0.0.1:2181");
        cfg.setClientId("master");
        cfg.setSessionTimeout(15000);
        
        MasterServer server = new MasterServer(cfg, new DefaultAssigner());
        
        server.start();
        
        TimeUnit.HOURS.sleep(1);
    }
}
