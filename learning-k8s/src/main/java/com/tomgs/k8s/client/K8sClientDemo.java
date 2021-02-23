package com.tomgs.k8s.client;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * k8s客户端操作
 *
 * @author tomgs
 * @since 2021/2/23
 */
public class K8sClientDemo {

  public static void main(String[] args) {
    Config config = new ConfigBuilder()
        .withMasterUrl("https://k8s-master.com")
        .build();
    // or
    //Config config = Config.fromKubeconfig(configContent);

    KubernetesClient client = new DefaultKubernetesClient(config);

    NamespaceList myNs = client.namespaces().list();
    //ServiceList myServices = client.services().list();
    //ServiceList myNsServices = client.services().inNamespace("default").list();
    System.out.println(myNs);
  }

}
