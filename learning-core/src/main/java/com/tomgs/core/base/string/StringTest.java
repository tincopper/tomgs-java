package com.tomgs.core.base.string;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/19 1.0 
 */
public class StringTest {

    public static void main(String[] args) {
        String str = "abc";

        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        StringBuilder reverse = sb.reverse();
        System.out.println(reverse.toString());
    }
}
