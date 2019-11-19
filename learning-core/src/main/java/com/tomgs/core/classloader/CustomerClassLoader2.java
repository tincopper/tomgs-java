package com.tomgs.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 *  
 *
 * @author tomgs
 * @version 2019/10/30 1.0 
 */
public class CustomerClassLoader2 extends URLClassLoader {

    public CustomerClassLoader2(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public CustomerClassLoader2(URL[] urls) {
        super(urls);
    }

    public CustomerClassLoader2(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public static void main(String[] args) {
        //Thread.currentThread().setContextClassLoader();
        //Thread.currentThread().getContextClassLoader();
    }

}
