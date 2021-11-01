package com.tomgs.camel.components.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;

/**
 * DubboProducer
 *
 * @author tomgs
 * @since 2021/10/26
 */
@Slf4j
public class DubboProducer extends DefaultProducer {

    private final DubboEndpoint endpoint;

    public DubboProducer(final DubboEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String[] registryEndpoints = endpoint.getRegistryEndpoints();
        log.info("exchange: {}", exchange);
    }

}
