package com.tomgs.scheduler.quartz.customer;

import com.tomgs.scheduler.quartz.customer.config.SchedulerConfig;
import com.tomgs.scheduler.quartz.customer.spi.QuartzScheduler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author tangzy
 * @since 1.0
 */
public class Main2 {

  public static void main(String[] args) throws Exception {
    int processors = Runtime.getRuntime().availableProcessors();
    // 这个序列化没啥用，序列化之后调度器里面的任务是空的。
    SchedulerConfig config = SchedulerConfig.builder().misfireThreshold(1).schedulerName("scheduler1").threadCount(processors / 2).build();
    SchedulerConfig config1 = SchedulerConfig.builder().misfireThreshold(1).schedulerName("scheduler2").threadCount(processors / 3).build();

    BasicScheduler scheduler = SchedulerManager.INSTANCE.createScheduler(config);
    BasicScheduler scheduler1 = SchedulerManager.INSTANCE.createScheduler(config1);

    //scheduler.config(config);
    //scheduler1.config(config1);

    NewJobRequest job = new NewJobRequest();
    job.setGroupName("groupName");
    job.setJobName("jobName");
    job.setCron("*/3 * * * * ?");
    scheduler.addJob(job);
    scheduler.start();
    System.out.println("---------------start1-------------");

    NewJobRequest job1 = new NewJobRequest();
    job1.setGroupName("groupName1");
    job1.setJobName("jobName1");
    job1.setCron("*/4 * * * * ?");
    scheduler1.addJob(job1);
    scheduler1.start();
    System.out.println("---------------start2-------------");
  }

  // 这种方式不行，这种一定要实现Serializable接口
  public static QuartzScheduler readObject() {
    FileInputStream fis;
    try {
      fis = new FileInputStream(new File("D://scheduler.tmp"));
      byte[] bytes = new byte[2048];
      int read;
      while ((read = fis.read(bytes)) != -1) {
        System.out.println(read);
      }
      return SerializeUtil.deserialize(bytes, QuartzScheduler.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void writeObject(Object o) throws IOException {
    byte[] serialize = SerializeUtil.serialize(o);
    FileOutputStream fos = new FileOutputStream(new File("D://scheduler.tmp"));
    fos.write(serialize);
    fos.flush();
    fos.close();
  }

}
