package com.tomgs.springboot.extend;

import java.util.Set;

/**
 * @author tomgs
 * @version 2020/7/5 1.0
 */
public interface ServiceProvider {

    Set<Class<?>> getServices();

    void loadService(Class<?> type);

}
