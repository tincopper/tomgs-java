package com.tomgs.es.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(EsProperties.class)
@ComponentScan("com.tomgs.es.*")
public class EsConfig {

    @Autowired
    private EsProperties esProperties;

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(TransportClient.class)
    public TransportClient client() throws UnknownHostException {

        // 指定集群名,默认为elasticsearch,如果改了集群名,这里一定要加
        Settings settings = Settings.builder()
                //设置ES实例的名称
                .put("cluster.name", esProperties.getClusterName())
                //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .put("client.transport.sniff", false)
                .put("client.transport.ping_timeout", "60000ms")
                .put("client.transport.nodes_sampler_interval", "60000ms")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);

        /*
        ES的TCP端口为9300,而不是之前练习的HTTP端口9200
        这里只配置了一个节点的地址然添加进去,也可以配置多个从节点添加进去再返回
         */
        TransportAddress node = new TransportAddress(
                InetAddress.getByName(esProperties.getHost()), esProperties.getTcpPort());
        client.addTransportAddress(node);

        System.out.println("连接" + esProperties.getHost() + ":" + esProperties.getTcpPort() + "成功...");

        return client;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(esProperties.getHost(), esProperties.getHttpPort(), "http"));
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        //设置每个请求需要发送的默认headers，这样就不用在每个请求中指定它们。
        builder.setDefaultHeaders(defaultHeaders);
        // 设置应该授予的超时时间，以防对相同的请求进行多次尝试。默认值是30秒，与默认socket超时时间相同。
        // 如果自定义socket超时时间，则应相应地调整最大重试超时时间。
        builder.setMaxRetryTimeoutMillis(10 * 60 * 1000);

        RestClient restClient = builder.build();
        System.out.println("连接" + esProperties.getHost() + ":" + esProperties.getHttpPort() + "成功...");
        return restClient;
    }

}
