package com.tomgs.es.demo.test;

import com.tomgs.es.restclient.EsHttpClient;
import com.tomgs.es.utils.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @since 2019-04-24 18:59
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRestClient {

    @Autowired
    private EsHttpClient esHttpClient;

    @Test
    public void performRequest() throws IOException {
        Response response = esHttpClient.performRequest("GET", "/");
        print(response);
    }

    @Test
    public void performRequestWithParam() throws IOException {
        //方式2：提供谓词和终节点以及一些查询字符串参数来发送请求
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        Response response = esHttpClient.performRequest("GET", "/", params);
        print(response);
    }

    private void print(Response response) throws IOException {
        System.out.println(response);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
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
        Response response = esHttpClient.performRequest(method, url, params, entity);
        print(response);
    }
}
