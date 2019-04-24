package com.tomgs.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * es入门demo
 *
 * @author tangzhongyuan
 * @since 2019-04-02 17:24
 **/
@SpringBootApplication
public class AppMain {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(AppMain.class, args);
    }
}
