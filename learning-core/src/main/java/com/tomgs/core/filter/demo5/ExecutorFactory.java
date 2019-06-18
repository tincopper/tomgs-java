package com.tomgs.core.filter.demo5;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *  过滤器factory
 *
 * @author tomgs
 * @version 2019/6/5 1.0 
 */
public class ExecutorFactory {
    private static final ServiceLoader<Executor> SERVICE_LOADER =
            ServiceLoader.load(Executor.class);

    public static List<Executor> getExecutorExtension() {
        return StreamSupport.stream(SERVICE_LOADER.spliterator(), false)
                .collect(Collectors.toList());
    }
}
