package com.tomgs.spring.core;

import java.util.EventObject;

/**
 * @author tomgs
 * @version 2020/12/6 1.0
 */
public interface Plugin {

    void init();

    void registerListener();

    void click(EventObject e);

    void itemClick(EventObject e);

}
