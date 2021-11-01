package com.tomgs.camel.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Test;

/**
 * HttpWithCamel
 *
 * @author tomgs
 * @since 2021/10/25
 */
public class HttpWithCamel {

    private final CamelContext context = new DefaultCamelContext();

    @After
    public void after() throws InterruptedException {
        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);

        // stop the CamelContext
        context.stop();
    }

    @Test
    public void testHttpProxy() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("http://").to("http://");
            }
        });
    }

    @Test
    public void testHttp2Grpc() {

    }

}
