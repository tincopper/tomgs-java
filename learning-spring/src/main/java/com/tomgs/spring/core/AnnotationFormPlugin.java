package com.tomgs.spring.core;

import java.util.EventObject;

/**
 *  
 *
 * @author tomgs
 * @version 2020/12/6 1.0 
 */
public class AnnotationFormPlugin extends AbstractFormPlugin {

    private final AnnotationRequestHandler handler;

    public AnnotationFormPlugin() {
        super();
        this.handler = AnnotationRequestHandler.getInstance();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void registerListener() {

    }

    @Override
    public void click(EventObject e) {
        Object source = e.getSource();
    }

    @Override
    public void itemClick(EventObject e) {

    }

}
