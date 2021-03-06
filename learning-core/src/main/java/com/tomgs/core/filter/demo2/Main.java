package com.tomgs.core.filter.demo2;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 19:57
 **/
public class Main {

    public static void main(String[] args) {
        FilterFacade filterFacade = new FilterFacade();
        new FilterDemo(filterFacade);
        new FilterDemo2(filterFacade);

        String str = "123";

        try {
            filterFacade.filter(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
