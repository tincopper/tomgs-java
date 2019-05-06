package com.tomgs.es.demo.test;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 11:42
 **/
public class TestCustomTransportClient {

    private TransportClient client;

    @Before
    public void client() throws UnknownHostException {
        String clusterName = "tomgs-es";
        String host = "10.18.4.23";
        int port = 9001;

        // 指定集群名,默认为elasticsearch,如果改了集群名,这里一定要加
        Settings settings = Settings.builder()
                //设置ES实例的名称
                .put("cluster.name", clusterName)
                .put("client.transport.ping_timeout", "100000ms")
                .put("client.transport.nodes_sampler_interval", "100000ms")
                //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                //.put("client.transport.sniff", true)
                .build();

        client = new PreBuiltTransportClient(settings);

        /*
        ES的TCP端口为9300,而不是之前练习的HTTP端口9200
        这里只配置了一个节点的地址然添加进去,也可以配置多个从节点添加进去再返回
         */
        TransportAddress node = new TransportAddress(
                InetAddress.getByName(host), port);
        client.addTransportAddress(node);

        System.out.println("连接" + host + ":" + port + "成功...");
    }

    @After
    public void close() {
        client.close();
    }

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
    public void testGetIndex() {

        IndexResponse indexResponse = client.prepareIndex("tomgs_index", "tomgs_type", "1").get();

        String id = indexResponse.getId();
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        long version = indexResponse.getVersion();
        RestStatus status = indexResponse.status();

        System.out.println(id + "#" + index + "#" + type + "#" + version + "#" + status);
    }
}
