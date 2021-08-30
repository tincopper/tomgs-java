package com.tomgs.core.base;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author tomgs
 * @since 2021/8/23
 */
public class ListDemo {

    @Test
    public void testAdd() {
        List<String> list = Lists.newArrayList("1", "2", "3");
        list.add(list.size() - 1, "4");
        System.out.println(list);
    }

}
