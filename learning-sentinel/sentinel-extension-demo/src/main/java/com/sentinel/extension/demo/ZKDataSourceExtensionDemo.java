package com.sentinel.extension.demo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tomgs.csp.sentinel.extension.common.config.ZookeeperDataSourceConfig;

import java.util.concurrent.TimeUnit;

/**
 *  -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=zk-datasource
 *
 * @author tomgs
 * @version 2020/2/10 1.0
 */
public class ZKDataSourceExtensionDemo {

    public static void main(String[] args) throws InterruptedException {
        ZookeeperDataSourceConfig.setRemoteAddress("localhost");
        new SentinelMonitor().start();

        //InitExecutor.doInit();
        //loadRules();
        // Assume we config: resource is `TestResource`, initial QPS threshold is 5.
        while (true) {
            Entry entry = null;
            try {
                entry = SphU.entry("TestResource", EntryType.IN, 1, 1);
                /*您的业务逻辑 - 开始*/
                System.out.println("hello world");
                //TimeUnit.MILLISECONDS.sleep(100);
                /*您的业务逻辑 - 结束*/
            } catch (BlockException e) {
                /*流控逻辑处理 - 开始*/
                System.out.println("block!");
                /*流控逻辑处理 - 结束*/
            } finally {
                if (entry != null) {
                    entry.exit(1, 1);
                }
            }

            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

}
