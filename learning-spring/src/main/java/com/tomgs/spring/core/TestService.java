package com.tomgs.spring.core;

/**
 *  test service
 *
 * @author tomgs
 * @version 2020/12/6 1.0 
 */
@Page("test_page")
public class TestService {

    @Click("test_add")
    public void testClick() {
        System.out.println("click...");
    }

    @Click("test_add1")
    public void testClick1(String test, Integer num) {
        System.out.println("click..." + test + num);
    }

    @ItemClick("test_item_add")
    public void testItemClick() {
        System.out.println("item click");
    }

}
