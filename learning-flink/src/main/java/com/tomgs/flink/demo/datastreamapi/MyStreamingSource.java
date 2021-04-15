package com.tomgs.flink.demo.datastreamapi;

import java.util.Random;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * 自定义流数据源
 *
 * @author tomgs
 * @since 2021/4/15
 */
public class MyStreamingSource implements SourceFunction<MyStreamingSource.Item> {

  private boolean isRunning = true;

  @Override
  public void run(SourceContext<Item> ctx) throws Exception {
    while (isRunning) {
      Item item = generateItem();
      ctx.collect(item);
      // 1s 产生一条
      Thread.sleep(1000);
    }

  }

  private Item generateItem() {
    int id = new Random().nextInt(100);
    Item item = new Item();
    item.setName("name" + id);
    item.setId(id);
    return item;
  }

  @Override
  public void cancel() {
    isRunning = false;
  }

  public static class Item {
    private String name;

    private Integer id;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "Item{" +
          "name='" + name + '\'' +
          ", id=" + id +
          '}';
    }
  }

}

