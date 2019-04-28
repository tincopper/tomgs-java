package com.tomgs.es.gateway;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.elasticsearch.common.component.LifecycleComponent;

import javax.inject.Named;

import static com.tomgs.es.gateway.module.EsGatewayModule.HTTP_SERVER;
import static com.tomgs.es.gateway.module.EsGatewayModule.TCP_SERVER;

/**
 * @author tangzhongyuan
 * @since 2019-04-25 17:07
 **/
@Singleton
public class ESGateway {

    private final LifecycleComponent tcpServer;
    private final LifecycleComponent httpServer;

    @Inject
    public ESGateway(@Named(TCP_SERVER) final LifecycleComponent tcpServer,
                     @Named(HTTP_SERVER) final LifecycleComponent httpServer) {
        this.tcpServer = tcpServer;
        this.httpServer = httpServer;
    }

    public void start() {
        this.tcpServer.start();
        this.httpServer.start();
    }

    public void stop() {
        this.tcpServer.stop();
        this.httpServer.stop();
    }
}
