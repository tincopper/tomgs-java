package com.tomgs.scheduler.quartz.customer;

import com.tomgs.scheduler.quartz.customer.spi.QuartzScheduler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ServiceLoader;

/**
 * @author tangzy
 * @since 1.0
 */
public class Main2 {

  public static void main(String[] args) throws Exception {
    // 这个序列化没啥用，序列化之后调度器里面的任务是空的。
    //QuartzScheduler bs = readObject();
    //if (bs != null) {
      //bs.start();
    //} else {
      ServiceLoader<BasicScheduler> schedulers = ServiceLoader.load(BasicScheduler.class);
      BasicScheduler scheduler = schedulers.iterator().next();

      ServiceLoader<BasicScheduler> schedulers1 = ServiceLoader.load(BasicScheduler.class);
      BasicScheduler scheduler1 = schedulers.iterator().next();

    if (scheduler == scheduler1) {
      System.out.println("=================");
    }

      NewJobRequest job = new NewJobRequest();
      scheduler.addJob(job);
      scheduler.start();
      System.out.println("---------------start-------------");
      // 序列化测试
      writeObject(scheduler);
    //}
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
