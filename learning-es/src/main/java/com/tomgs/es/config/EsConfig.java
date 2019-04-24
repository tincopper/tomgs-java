package com.tomgs.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * es 配置实例化
 *
 * @author tangzhongyuan
 * @since 2019-04-04 18:23
 **/
@Configuration
@ComponentScan("com.tomgs.es.*")
public class EsConfig {

    @Value("${es.cluster.name}")
    private String clusterName;

    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private int port;

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(TransportClient.class)
    public TransportClient client() throws UnknownHostException {

        // 指定集群名,默认为elasticsearch,如果改了集群名,这里一定要加
        Settings settings = Settings.builder()
                //设置ES实例的名称
                .put("cluster.name", clusterName)
                //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                //.put("client.transport.sniff", true)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);

        /*
        ES的TCP端口为9300,而不是之前练习的HTTP端口9200
        这里只配置了一个节点的地址然添加进去,也可以配置多个从节点添加进去再返回
         */
        TransportAddress node = new TransportAddress(
                InetAddress.getByName(host), port);
        client.addTransportAddress(node);

        System.out.println("连接" + host + ":" + port + "成功...");

        return client;
    }

}
