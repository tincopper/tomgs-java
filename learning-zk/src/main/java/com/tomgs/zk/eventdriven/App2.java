package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App2 
{
    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException
    {
        Configuration cfg = new Configuration();
        cfg.setNamespace("/event-driven");
        cfg.setZkAddrs("127.0.0.1:2181");
        cfg.setClientId("host4");
        cfg.setSessionTimeout(15000);
        
        WorkServer server = new WorkServer(cfg);
        
        server.start((type) -> Arrays.asList(new BaseSubscriber[]{(e) -> System.out.println(e.getVal())}));
        
        TimeUnit.HOURS.sleep(1);
    }
}
