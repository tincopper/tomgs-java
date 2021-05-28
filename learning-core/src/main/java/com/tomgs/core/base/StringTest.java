package com.tomgs.core.base;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

/**
 * @author tomgs
 * @version 2019/10/30 1.0
 */
public class StringTest {

    public static void main(String[] args) {
        String s = "hello";
        String s1 = "he" + new String("llo");
        System.out.println(s == s1); // false
        ////////////////////////////
        String s2 = new String("hello");
        System.out.println(s == s2);  //false

        String s3 = s2.intern();
        System.out.println(s == s3);  //true
        System.out.println(s2 == s3);  //false
        System.out.println(s1 == s3);  //false

        s = s + 1;
        int length = s.length();

    }

    @Test
    public void testSubstr() {
        String str = "task buildTest()";
        String substring = str.substring(5, str.indexOf("("));
        System.out.println(substring);
    }

    @Test
    public void test() {
        Object o = true;
        o = "ssss";
        System.out.println(o);
    }

    @Test
    public void test2() {
        Path path = Paths.get("a", "b", "c");
        System.out.println(path.toString());
    }

}
