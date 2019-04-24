package com.tomgs.guice.test;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 11:15
 **/
public class GoodPlayer implements Player {

    @Override
    public void bat() {
        System.out.println("I can hit any ball");
    }

    @Override
    public void bowl() {
        System.out.println("I can also bowl");
    }
}
