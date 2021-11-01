package com.tomgs.camel.components.dubbo;

import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;

import java.util.Map;

/**
 * DubboComponent
 *
 * @author tomgs
 * @since 2021/10/26
 */
public class DubboComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // dubbo://localhost:2181/org.apache.camel.examples.CamelHello?method=sayHelloToCamel&synchronous=true
        return new DubboEndpoint(uri, this);
    }

}
