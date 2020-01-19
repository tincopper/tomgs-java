package com.tomgs.core.map;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangzhongyuan
 * @since 2019-05-29 11:23
 **/
public class TestMap {

    //private final Map<String, String> map = new HashMap<>();
    private static volatile Map<String, String> map = new HashMap<>();
    //private final Map<String, String> map = new Hashtable<>();
    //private final Map<String, String> map = new ConcurrentHashMap<>();

    private static final ReentrantLock LOCK = new ReentrantLock();

    @Before
    public void setup() {
        map.put("a", "123");
    }

    @Test
    public void testPutValueToMap() {
        map.computeIfAbsent("a", k -> "1");
    }

    @Test
    public void testPut() {
        //hashmap 是可以允许key为null或者value为null的情况，key为null的情况hash==0，数据存放在数组的第0个位置
        //concurrentHashMap和hashtable是不允许key为null或者value为null的情况，会报空指针异常，ConcurrentHashMap.java:1011
        map.put(null, null);
        map.put("123", null);
        map.put(null, "123");

        System.out.println(map);
    }

    /**
     * HashMap扩容，如何实现在不影响读写的情况下扩容？
     * org.apache.commons.collections.FastHashMap
     * io.netty.util.collection.IntObjectHashMap
     */
    @Test
    public void testMapUsing() throws IOException, InterruptedException {
        Map<String, String> localCachedNameMap = map;

        new Thread(() -> {
            for (;;) {
                System.out.println(LOCK.isLocked());
                map.forEach((k, v) -> {
                    System.out.println("map:" + k + ":" + v);
                });
                localCachedNameMap.forEach((k, v) -> {
                    System.out.println("cached:" + k + ":" + v);
                });
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "test").start();

        try {
            // 这个加锁会影响其他线程读取map的值，但是不会影响其他线程的localCachedNameMap的值
            LOCK.lock();
            Map<String, String> newMap = new HashMap<>(map.size() + 1);
            newMap.putAll(map);
            newMap.put("a", "12321");
            // 这一步赋值不会影响 Map<String, String> localCachedNameMap = this.map; 这一步
            // 所以在多线程的时候，操作map这个变量不会引起读写冲突
            // https://www.cnblogs.com/yuyutianxia/p/6255658.html
            // 这样在map进行扩容的时候不会影响原来map的读写
           map = newMap;
           TimeUnit.SECONDS.sleep(30);
        } finally {
            LOCK.unlock();
        }

        System.in.read();
    }

}
