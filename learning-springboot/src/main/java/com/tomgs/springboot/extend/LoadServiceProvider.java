package com.tomgs.springboot.extend;

import com.google.common.collect.Sets;
import java.util.Set;

/**
 *  
 *
 * @author tomgs
 * @version 2020/7/5 1.0 
 */
public class LoadServiceProvider implements ServiceProvider {

    private final Set<Class<?>> classSet;

    public LoadServiceProvider() {
        this.classSet = Sets.newConcurrentHashSet();
    }

    @Override
    public Set<Class<?>> getServices() {
        return classSet;
    }

    @Override
    public void loadService(Class<?> type) {
        classSet.add(type);
    }

}
