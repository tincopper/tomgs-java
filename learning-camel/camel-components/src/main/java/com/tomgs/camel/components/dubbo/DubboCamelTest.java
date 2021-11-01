package com.tomgs.camel.components.dubbo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.CountDownLatch;

/**
 * DubboCamelTest
 *
 * @author tomgs
 * @since 2021/10/26
 */
public class DubboCamelTest {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:http://localhost:8080/api/sayHello")
                        .process(new DefaultInProcessor())
                        .to("dubbo://localhost:2181?interfaceClass=com.tomgs.learning.dubbo.api.IGreeter&methodName=sayHello2");

                /*from("grpc://localhost:1101/org.apache.camel.component.grpc.PingPong?consumerStrategy=PROPAGATION")
                        .to("direct:grpc-service");*/
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);
        new CountDownLatch(1).await();
        // stop the CamelContext
        //context.stop();
    }

}
