package com.tomgs.learning.logger.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义日志实现demo1
 *
 * <pre>
 * 日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL
 * intLevel值依次为0,100,200,300,400,500,600,700
 * intLevel 值越小，级别越高
 * </pre>
 *
 * @author tangzhongyuan
 * @since 2019-06-20 18:23
 **/
public class Demo1Main {

    private static final Logger log = LoggerFactory.getLogger(Demo1Main.class);

    private static final org.apache.logging.log4j.Logger customerLog = LogManager.getLogger(Demo1Main.class);

    public static void main(String[] args) {
        log.info("hello world {}.", 1);

        customerLog.log(Level.forName("CUSTOMER", 50), "customer logger [{}]", 50);
    }
}
