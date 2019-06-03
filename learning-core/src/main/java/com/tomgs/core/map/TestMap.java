package com.tomgs.core.map;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangzhongyuan
 * @since 2019-05-29 11:23
 **/
public class TestMap {

    private final Map<String, String> map = new ConcurrentHashMap<>();

    @Before
    public void setup() {

    }

    @Test
    public void testPutValueToMap() {
        map.computeIfAbsent("a", k -> "1");
    }
}
