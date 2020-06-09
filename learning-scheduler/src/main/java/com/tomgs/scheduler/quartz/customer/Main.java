package com.tomgs.scheduler.quartz.customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ServiceLoader;

/**
 * @author tangzy
 * @since 1.0
 */
public class Main {

  public static void main(String[] args) throws Exception {
    BasicScheduler bs = (BasicScheduler)readObject();
    if (bs != null) {
      bs.start();
    } else {
      ServiceLoader<BasicScheduler> schedulers = ServiceLoader.load(BasicScheduler.class);
      BasicScheduler scheduler = schedulers.iterator().next();
      NewJobRequest job = new NewJobRequest();
      scheduler.addJob(job);
      scheduler.start();
      System.out.println("---------------start-------------");
      // 序列化测试
      writeObject(scheduler);
    }
  }

  // 这种方式不行，这种一定要实现Serializable接口
  public static Object readObject() {
    FileInputStream fis;
    try {
      fis = new FileInputStream(new File("D://scheduler.tmp"));
      ObjectInputStream ois = new ObjectInputStream(fis);
      return ois.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void writeObject(Object o) throws IOException {
    FileOutputStream fos = new FileOutputStream(new File("D://scheduler.tmp"));
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(o);
    oos.close();
  }

}
