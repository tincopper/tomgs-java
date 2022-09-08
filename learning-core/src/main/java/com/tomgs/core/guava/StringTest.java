package com.tomgs.core.guava;

import com.google.common.base.CaseFormat;
import org.junit.Test;

/**
 * StringTest
 *
 * @author tomgs
 * @since 2022/9/7
 */
public class StringTest {

    @Test
    public void testCaseFormat() {
        String str = "hello_world";
        final String result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);//转换成helloWorld
        final String result1 = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);//转换成HelloWorld
        System.out.println(result);
        System.out.println(result1);

        str = "GET_helloworld";
        final String result2 = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);//转换成helloWorld
        final String result3 = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);//转换成HelloWorld
        System.out.println(result2);
        System.out.println(result3);
    }

}
