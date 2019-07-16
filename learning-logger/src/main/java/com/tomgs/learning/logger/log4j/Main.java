package com.tomgs.learning.logger.log4j;

/**
 * @author tangzhongyuan
 * @since 2019-07-02 20:05
 **/
public class Main {

    public static void main(String[] args) {
       /* try {
            throw new RuntimeException("===========");
        } finally {
            System.out.println("-------------");
        }*/
        System.out.println(test());
    }

    public static int test() {
        int i = 0;
        try {
           return ++i;
        } finally {
            i = 3;
        }
    }
}
