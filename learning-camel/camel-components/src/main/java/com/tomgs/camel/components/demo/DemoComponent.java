package com.tomgs.camel.components.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;

import java.util.Map;

/**
 * DemoComponenet
 *
 * @author tomgs
 * @since 2021/10/26
 */
@Slf4j
public class DemoComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        DemoEndpoint endpoint = new DemoEndpoint(uri, this);
        setProperties(endpoint, parameters);
        log.info("#### DemoComponent create endpoint.");
        return endpoint;
    }

}
