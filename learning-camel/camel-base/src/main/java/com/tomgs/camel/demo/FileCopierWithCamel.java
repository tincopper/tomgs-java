package com.tomgs.camel.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * FileCopierWithCamel
 *
 * @author tomgs
 * @since 2021/10/25
 */
@Slf4j
public class FileCopierWithCamel {

    public static void main(String[] args) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();
        log.info("aaaaaaaaaaaaaaa");
        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                /*
                 file: 表示使用文件Component
                 from 表示从哪里获取数据，进行消费
                 to  表示将数据生产到哪里
                 */
                from("file:data/inbox?noop=true").to("file:data/outbox");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);

        // stop the CamelContext
        context.stop();
    }
}