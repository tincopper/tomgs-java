package com.tomgs.springboot.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 *
 * @author tangzhongyuan
 * @since 2019-08-05 10:33
 **/
@Component
public class ScheduleTask {

    @Scheduled(fixedRate = 1000)
    public void task1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("执行 fixedRate 任务的时间：" + new Date(System.currentTimeMillis()));
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void task2() {
        System.out.println("执行 cron 任务的时间：" + new Date(System.currentTimeMillis()));
    }


}
