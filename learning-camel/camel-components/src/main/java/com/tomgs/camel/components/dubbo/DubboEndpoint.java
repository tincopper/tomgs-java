package com.tomgs.camel.components.dubbo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
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
 * DubboEndpoint
 *
 * @author tomgs
 * @since 2021/10/26
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
@UriEndpoint(scheme = "dubbo",
        syntax = "dubbo://{registryEndpoints}?interfaceClass={interfaceClass}&method={methodName}",
        title = "dubbo component endpoint",
        producerOnly = true)
public class DubboEndpoint extends DefaultEndpoint {

    @UriPath(name = "registryEndpoints", description = "注册中心地址")
    private String[] registryEndpoints;

    @UriParam(name = "interfaceClass", description = "dubbo接口类名")
    @Metadata(required = true)
    private String interfaceClass;

    @UriParam(name = "version", description = "接口版本")
    private String version;

    @UriParam(name = "methodName", description = "接口方法名")
    private String methodName;

    @UriParam(name = "parameterNames", description = "接口参数key值")
    private String[] parameterNames;

    @UriParam(name = "parameterTypes", description = "接口参数类型")
    private String[] parameterTypes;

    public DubboEndpoint(String uri, Component component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new DubboProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new IllegalArgumentException("camel dubbo not support consumers...");
    }

}
