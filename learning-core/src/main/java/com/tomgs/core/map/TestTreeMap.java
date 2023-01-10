package com.tomgs.core.map;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * TestTreeMap
 *
 * @author tomgs
 * @since 1.0
 */
public class TestTreeMap {

    private final TreeMap<String, String> map = new TreeMap<>();

    @Before
    public void setup() {
        map.put("a/b/c", "1");
        map.put("a/b/d", "2");
        map.put("a/b/e", "3");
    }

    @Test
    public void testGet() {
        final String result = map.floorKey("a/b/");
        System.out.println(result);
    }

    @Test
    public void testNavigableMap() {
        NavigableMap<String, String> map = new TreeMap<>();
        map.put("a/b/c", "1");
        map.put("a/b/d", "2");
        map.put("a/b/e", "3");
        map.put("a/c/e", "4");
        map.put("a/d/e", "5");

        System.out.println("Values starting from abc");
        // expected output value0, value1, value2, value4

        final String key = "a/b";
        System.out.println(map.floorKey(key));
        System.out.println(map.ceilingKey(key));
        System.out.println(map.lowerKey(key));
        System.out.println(map.higherKey(key));

        searchByPrefix(map, key)
                .forEach(System.out::println);
    }

    private Collection<String> searchByPrefix(NavigableMap<String, String> map, String begin) {
        String end = endKey(begin);
        // map.subMap(fromKey, fromInclusive, toKey, toInclusive), toKey should not be inclusive for our case
        return map.subMap(begin, true, end, false).values();
    }

    public String endKey(String prefix) {
        return prefix.substring(0, prefix.length() - 1) + (char) (prefix.charAt(prefix.length() - 1) + 1);
    }

}
