package com.tomgs.guice.test;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 11:16
 **/
public class BadPlayer implements Player {

    @Override
    public void bat() {
        System.out.println("I think i can face the ball");
    }

    @Override
    public void bowl() {
        System.out.println("I dont know bowling");
    }
}
