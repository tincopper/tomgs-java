package com.tomgs.es.demo.test;

import com.tomgs.es.utils.JsonUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 11:07
 **/
public class TestCustomRestClient {

    private RestClient restClient;

    @Before
    public void restClient() {
        // local es gateway
        String host = "127.0.0.1";
        int port = 8080;

        // local es server
        //int port = 9200;

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, "http"));
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        //设置每个请求需要发送的默认headers，这样就不用在每个请求中指定它们。
        builder.setDefaultHeaders(defaultHeaders);
        // 设置应该授予的超时时间，以防对相同的请求进行多次尝试。默认值是30秒，与默认socket超时时间相同。
        // 如果自定义socket超时时间，则应相应地调整最大重试超时时间。
        builder.setMaxRetryTimeoutMillis(100000);

        restClient = builder.build();
        System.out.println("连接" + host + ":" + port + "成功...");
    }

    @After
    public void close() throws IOException {
        if (restClient != null) {
            restClient.close();
        }
    }

    @Test
    public void testPerformRequest() throws IOException {
        Response response = restClient.performRequest("GET", "/");
        print(response);
    }

    @Test
    public void performRequestWithParam() throws IOException {
        //方式2：提供谓词和终节点以及一些查询字符串参数来发送请求
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        Response response = restClient.performRequest("GET", "/", params);
        print(response);
    }

    @Test
    public void createIndex() throws IOException {
        String method = "PUT";
        String url = "/tomgs_index/tomgs_type/1";

        //这个map是uri中?的参数
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        //设置请求体
        HashMap<String, String> sourceMap = new HashMap<>();
        sourceMap.put("user", "tomgs");
        sourceMap.put("age", "18");

        String jsonString = JsonUtil.toJson(sourceMap);

        //为HttpEntity指定ContentType非常重要，因为它将用于设置Content-Type请求头，以便Elasticsearch可以正确解析内容。
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(method, url, params, entity);
        print(response);
    }

    @Test
    public void getIndex() throws IOException {
        String method = "GET";
        String url = "/tomgs_index/tomgs_type/1";
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        Response response = restClient.performRequest(method, url, params);
        print(response);
    }

    private void print(Response response) throws IOException {
        System.out.println(response);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
    }

}
