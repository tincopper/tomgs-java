package com.tomgs.camel.components.demo;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;

/**
 * DemoEndpoint
 *
 * @author tomgs
 * @since 2021/10/26
 */
@UriEndpoint(scheme = "demo", syntax = "demo", title = "demo component", producerOnly = true)
public class DemoEndpoint extends DefaultEndpoint {

    @UriPath(
            name = "registryAddress",
            description = "注册中心地址"
    )
    private String[] address;

    @UriParam(
            name = "interfaceClass",
            description = "dubbo接口类名"
    )
    @Metadata(required = true)
    private String interfaceClass;
    @UriParam(name = "version",description = "接口版本")
    private String version;
    @UriParam(name = "methodName",description = "接口方法名")
    private String methodName;


    @UriParam(name = "parameterNames",description = "接口参数key值")
    private String[] parameterNames;
    @UriParam(name = "parameterTypes",description = "接口参数类型")
    private String[] parameterTypes;

    public DemoEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new DemoProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw  new UnsupportedOperationException("不支持消费者模式...");
    }

}
