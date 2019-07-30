package com.tomgs.core.map;

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

    private final Map<String, String> map = new HashMap<>();
    //private final Map<String, String> map = new Hashtable<>();
    //private final Map<String, String> map = new ConcurrentHashMap<>();

    @Before
    public void setup() {

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
}
