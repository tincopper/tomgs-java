package com.tomgs.es.demo.test;

import com.tomgs.es.utils.JsonUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 11:07
 **/
public class TestCustomRestClient2 {

    private RestClient restClient;

    @Before
    public void restClient() {
        // local es gateway
        String host = "es-pro-softlayer.gw-ec.com";
        int port = 80;

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, "http"));

        String base = "elastic" + ":" + "Hqyg@2019";
        String token = "Basic " + Base64.getEncoder().encodeToString(base.getBytes(StandardCharsets.UTF_8));
        Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization", token)};
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
    public void testLoadDataFromJson() throws IOException {
        final String path = ClassLoader.getSystemResource("data/data.json").getPath();
        final List list = JsonUtil.parseObject(new FileInputStream(new File(path)), ArrayList.class);
        final String method = "PUT";
        String urlFormat = "/%s/%s/%s";
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        int i = 0;
        try {
            for (Object o : list) {
                final Map<String, Object> map = JsonUtil.convertObjectToMap(o);
                final String index = String.valueOf(map.get("_index"));
                final String id = String.valueOf(map.get("_id"));
                final String type = String.valueOf(map.get("_type"));
                final Object source = map.get("_source");

                String jsonString = JsonUtil.toJson(source);
                final String encodeId = URLEncoder.encode(id, "utf-8");
                final String url = String.format(urlFormat, index, type, encodeId);
                HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
                Response response = restClient.performRequest(method, url, params, entity);
                print(response);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("======> count::" + i);
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


    private void print(Response response) throws IOException {
        System.out.println(response);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
    }

}
