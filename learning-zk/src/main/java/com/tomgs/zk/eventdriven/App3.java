package com.tomgs.zk.eventdriven;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App3 
{
    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException
    {
        Configuration cfg = new Configuration();
        cfg.setNamespace("/event-driven");
        cfg.setZkAddrs("127.0.0.1:2181");
        cfg.setSessionTimeout(15000);
        
        Client client = new Client(cfg);
        
        Event event = new Event();
        event.setId(1l);
        event.setType("type");
        event.setVal("{\"name\":\"欧阳\"}");
        event.setGroup("p-5");
        
        for (int i=1; i<=20; i++) {
            client.publish(event);
            event.setId(event.getId()+1);
        }
        
        client.monitor();
        
        client.close();
    }
}
