package com.tomgs.scheduler.schedulerX;

import com.alibaba.schedulerx.worker.SchedulerxWorker;

/**
 * SchedulerXDemo
 *
 * @author tomgs
 * @since 1.0
 */
public class SchedulerXDemo {

    public static void main(String[] args) throws Exception {
        initSchedulerxWorker();
    }

    public static void initSchedulerxWorker() throws Exception {
        SchedulerxWorker schedulerxWorker = new SchedulerxWorker();
        schedulerxWorker.setDomainName("localhost:8008/hodor/schedulerx");
        schedulerxWorker.setEndpoint("localhost:8008/hodor/schedulerx");
        schedulerxWorker.setNamespace("test");
        schedulerxWorker.setNamespaceSource("testSource");
        schedulerxWorker.setGroupId("testGroup");
        //1.2.1及以上版本需要设置应用key
        schedulerxWorker.setAppKey("123");
        schedulerxWorker.init();
    }
}
