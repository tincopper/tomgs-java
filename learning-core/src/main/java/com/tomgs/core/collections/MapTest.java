package com.tomgs.core.collections;

import org.junit.Test;

import java.util.*;

/**
 * @author tomgs
 * @since 2021/8/3
 */
public class MapTest {

    @Test
    public void testMapRemove() {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        map.put("a", set);

        System.out.println(map);

        // lambada 方式删除集合元素
        String nodeEndpoint = "a";
        Optional.ofNullable(map.get("a")).ifPresent(endpoints -> endpoints.removeIf(endpoint -> endpoint.equals(nodeEndpoint)));

        System.out.println(map);
    }

}
