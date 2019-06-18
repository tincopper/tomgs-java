package com.tomgs.core.filter.demo5;

import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 19:57
 **/
public class Main {

    public static void main(String[] args) {
        List<Filter> filters = FilterFactory.getFilterExtension();
        List<Executor> invokers = ExecutorFactory.getExecutorExtension();
        FilterFacade filterFacade = new FilterFacade(filters, invokers);

        String str = "123";
        try {
            filterFacade.filter(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
