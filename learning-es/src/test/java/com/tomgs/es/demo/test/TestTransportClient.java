package com.tomgs.es.demo.test;

import com.tomgs.es.AppMain;
import com.tomgs.es.tcpclient.EsTcpClient;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @since 2019-04-24 11:00
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppMain.class)
public class TestTransportClient {

    @Autowired
    private EsTcpClient esTcpClient;

    @Autowired
    private TransportClient client;

    @Test
    public void testTransportClient() {
        HashMap<String, String> sourceMap = new HashMap<>();
        sourceMap.put("user", "tomgs");
        sourceMap.put("age", "18");
        IndexResponse indexResponse = client.prepareIndex("tomgs_index", "tomgs_type", "1")
                .setSource(sourceMap, XContentType.JSON).get();

        String id = indexResponse.getId();
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        long version = indexResponse.getVersion();
        RestStatus status = indexResponse.status();

        System.out.println(id + "#" + index + "#" + type + "#" + version + "#" + status);
    }

    @Test
    public void testPrepareIndex() {

        System.setProperty("es.set.netty.runtime.available.processors", "false");

        HashMap<String, String> sourceMap = new HashMap<>();
        sourceMap.put("user", "tomgs");
        sourceMap.put("age", "18");

        IndexResponse indexResponse = esTcpClient.prepareIndex("tomgs_index", "tomgs_type", "1", sourceMap);

        String id = indexResponse.getId();
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        long version = indexResponse.getVersion();
        RestStatus status = indexResponse.status();

        System.out.println(id + "#" + index + "#" + type + "#" + version + "#" + status);
    }

    @Test
    public void testPrepareGet() {
        GetResponse indexResponse = esTcpClient.prepareGet("tomgs_index", "tomgs_type", "1");
        String id = indexResponse.getId();
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        long version = indexResponse.getVersion();
        Map<String, Object> source = indexResponse.getSource();

        System.out.println(id + "#" + index + "#" + type + "#" + version + "#" + source);
    }

}

