package com.tomgs.scheduler.quartz.customer.reg;

import com.google.common.base.Charsets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/4/19
 */
public class ZkClientDemo {

  private CuratorFramework zkClient;

  @Before
  public void before() {
    String zkServerAddress = "127.0.0.1:2181";
    ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
    zkClient = CuratorFrameworkFactory.builder()
        .namespace("elog")
        .connectString(zkServerAddress)
        .sessionTimeoutMs(5000)
        .connectionTimeoutMs(5000)
        .retryPolicy(retryPolicy)
        .build();
    zkClient.start();
  }

  @Test
  public void createPath() throws Exception {
    String parentDir = "/erms/datasource";
    if (zkClient.checkExists().forPath(parentDir) == null) {
      zkClient.create().creatingParentsIfNeeded().forPath(parentDir);
//      zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(parentDir);
    }
  }

}
