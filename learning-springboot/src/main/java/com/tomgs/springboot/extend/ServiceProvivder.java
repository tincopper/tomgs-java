package com.tomgs.springboot.extend;

import java.util.List;

/**
 * @author tomgs
 * @version 2020/7/5 1.0
 */
public interface ServiceProvivder {

    List<Class<?>> getServices();

    void loadService();

}
