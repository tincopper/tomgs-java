package com.tomgs.es.restclient;

import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @since 2019-04-24 18:39
 **/
@Component
public class EsHttpClient {

    @Autowired
    private RestClient restClient;

    public Response performRequest(String method, String endpoint) throws IOException {
        return restClient.performRequest(method, endpoint);
    }

    public Response performRequest(String method, String endpoint, Map<String, String> params) throws IOException {
        return restClient.performRequest(method, endpoint, params);
    }

    public Response performRequest(String method, String url, Map<String, String> params, HttpEntity entity) throws IOException {
        return restClient.performRequest(method, url, params, entity);
    }
}
