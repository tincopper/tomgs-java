package com.tomgs.core.unsafe;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * UnsafeDemo
 *
 * https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html
 *
 * @author tomgs
 * @since 2021/11/2
 */
@Slf4j
public class UnsafeDemo {

    /**
     * 那如若想使用这个类，该如何获取其实例？有如下两个可行方案。
     *
     * 其一，从getUnsafe方法的使用限制条件出发，通过Java命令行命令-Xbootclasspath/a把调用Unsafe相关方法的类A所在jar包路径追加到默认的bootstrap路径中，使得A被引导类加载器加载，从而通过Unsafe.getUnsafe方法安全的获取Unsafe实例。
     *
     * java -Xbootclasspath/a: ${path}   // 其中path为调用Unsafe相关方法的类所在jar包路径
     * 其二，通过反射获取单例对象theUnsafe。
     */
    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
