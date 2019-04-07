package com.tomgs.es.tcpclient;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tangzhongyuan
 * @create 2019-04-04 18:23
 **/
@Component
public class EsTcpClient {

    @Autowired
    private TransportClient transportClient;


}
