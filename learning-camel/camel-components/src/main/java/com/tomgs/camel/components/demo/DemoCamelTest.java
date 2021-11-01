package com.tomgs.camel.components.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * DemoCamelTest
 *
 * @author tomgs
 * @since 2021/10/26
 */
public class DemoCamelTest {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        // 手动添加component 或者通过SPI的方式
        // context.addComponent("demo", new DemoComponent());

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data/inbox?noop=true").to("demo:data/outbox");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);

        // stop the CamelContext
        context.stop();
    }

}
