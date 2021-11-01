package com.tomgs.camel.components.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;

/**
 * DemoProducer
 *
 * @author tomgs
 * @since 2021/10/26
 */
@Slf4j
public class DemoProducer extends DefaultProducer {

    private final DemoEndpoint endpoint;

    public DemoProducer(DemoEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("DemoProducer processing ...");
    }

}
