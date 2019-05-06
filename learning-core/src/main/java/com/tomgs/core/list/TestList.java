package com.tomgs.core.list;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 *
 * @author tomgs
 * @version 2019/4/29 1.0 
 */
public class TestList {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("a");
        list.add("b");

        list.remove("c");
    }
    
}
