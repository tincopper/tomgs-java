package com.tomgs.sentinel;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.client.exception.ApolloOpenApiException;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import java.util.List;

// https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
// 支持spring AOP和AspectJ的配置形式（这个不推荐）
public class AopDemo {

  private static String appId = "sentinel-demo";
  private static String env = "DEV";
  private static String clusterName = "default";
  private static String namespace = "application";

  public static void main(String[] args) {
    ApolloOpenApiClient client = ApolloOpenApiClient.newBuilder()
        .withPortalUrl("http://172.20.183.155:8070")
        .withToken("db6cf469a94e38b18e89777a97931f6a02c974ca")
        .build();

    String token = client.getToken();
    System.out.println(token);

    List<OpenNamespaceDTO> namespaces = client.getNamespaces(appId, env, clusterName);
    System.out.println(namespaces);

    OpenItemDTO openItemDTO = new OpenItemDTO();
    openItemDTO.setKey("test");
    openItemDTO.setValue("test1");
    openItemDTO.setComment("123");
    openItemDTO.setDataChangeCreatedBy("apollo");

    //client.createOrUpdateItem(appId, env, clusterName, namespace, openItemDTO);
    //client.createItem(appId, env, clusterName, namespace, openItemDTO);
    //client.updateItem(appId, env, clusterName, namespace, openItemDTO);
    // 兼容低版本操作
    ApolloOpenApiException apolloOpenApiException = null;
    try {
      client.createOrUpdateItem(appId, env, clusterName, namespace, openItemDTO);
    } catch (Exception e) {
      if (!(e.getCause() instanceof ApolloOpenApiException)) {
        throw e;
      }
      apolloOpenApiException = (ApolloOpenApiException) e.getCause();
    }
    if (apolloOpenApiException == null) {
      return;
    }
    if (404 != apolloOpenApiException.getStatus()) {
      throw apolloOpenApiException;
    }
    client.createItem(appId, env, clusterName, namespace, openItemDTO);
  }

}
