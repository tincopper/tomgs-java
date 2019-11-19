package com.tomgs.core.classloader;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/13 1.0 
 */
public class CustomerClassLoader3 extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        return super.findClass(name);
    }
}
