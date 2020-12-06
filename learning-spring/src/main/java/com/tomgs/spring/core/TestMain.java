package com.tomgs.spring.core;

import java.util.EventObject;

/**
 *  
 *
 * @author tomgs
 * @version 2020/12/6 1.0 
 */
public class TestMain {

    public static void main(String[] args) {
        AnnotationRequestHandler handler = AnnotationRequestHandler.getInstance();
        handler.doScanAnnotation("com.tomgs.spring.core");

        EventObject e = new EventObject("test change");

        AnnotationFormPlugin plugin = new AnnotationFormPlugin();
        plugin.init();
        plugin.registerListener();
        plugin.click(e);
        plugin.itemClick(e);
    }
    
}
