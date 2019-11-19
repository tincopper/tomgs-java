package com.tomgs.core.classloader.watchfile;

/**
 *  
 *
 * @author tomgs
 * @version 2019/10/30 1.0 
 */
public class CustomerClassLoader3 extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

}
