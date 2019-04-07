package com.tomgs.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 *
 * @author tangzhongyuan
 * @create 2019-04-04 18:23
 **/
@Configuration
@Component
public class EsConfig {

    @Value("es.cluster.name")
    private String clusterName;

    @Value("es.host")
    private String host;

    @Value("es.port")
    private int port;

    @Bean
    public TransportClient client() throws UnknownHostException {

        // 指定集群名,默认为elasticsearch,如果改了集群名,这里一定要加
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);

        /*
        ES的TCP端口为9300,而不是之前练习的HTTP端口9200
        这里只配置了一个节点的地址然添加进去,也可以配置多个从节点添加进去再返回
         */
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName(host),
                port
        );
        client.addTransportAddress(node);

        return client;
    }
}
