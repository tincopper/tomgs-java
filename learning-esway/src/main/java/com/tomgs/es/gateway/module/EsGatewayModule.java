package com.tomgs.es.gateway.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.tomgs.es.gateway.common.Props;
import com.tomgs.es.gateway.transport.http.netty4.NettyHttpServer;
import com.tomgs.es.gateway.transport.http.netty4.v2.DefaultHttpHandler;
import com.tomgs.es.gateway.transport.http.netty4.v2.HttpHandler;
import com.tomgs.es.gateway.transport.tcp.netty4.Netty4Transport;
import com.tomgs.es.gateway.transport.tcp.netty4.NettyTcpServer;
import org.elasticsearch.Version;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.network.NetworkService;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.PageCacheRecycler;
import org.elasticsearch.indices.breaker.NoneCircuitBreakerService;
import org.elasticsearch.node.Node;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportSettings;

import javax.inject.Named;
import java.util.Collections;

import static org.elasticsearch.common.settings.Settings.builder;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 14:15
 **/
public class EsGatewayModule extends AbstractModule {

    public static final String TCP_SERVER = "TCP_SERVER";
    public static final String HTTP_SERVER = "HTTP_SERVER";

    public static final String SERVER_TCP_HOST = "server.tcp.host";
    public static final String SERVER_TCP_PORT = "server.tcp.port";
    public static final String SERVER_HTTP_HOST = "server.http.host";
    public static final String SERVER_HTTP_PORT = "server.http.port";

    @Override
    protected void configure() {
        //bind(ESGateway.class).toInstance(new ESGateway());
        bind(HttpHandler.class).to(DefaultHttpHandler.class).in(Scopes.SINGLETON);
    }

    @Singleton
    @Provides
    @Named(TCP_SERVER)
    private LifecycleComponent createNettyTcpServer(Props props) {
        String host = props.getString(SERVER_TCP_HOST, "127.0.0.1");
        int port = props.getInt(SERVER_TCP_PORT, 9300);

        return new NettyTcpServer(host, port);
    }

    private LifecycleComponent createNettyTcpServer2(Props props) {
        String host = props.getString(SERVER_TCP_HOST, "127.0.0.1");
        int port = props.getInt(SERVER_TCP_PORT, 9300);

        Settings clientSettings = builder()
                //.put(TcpTransport.PING_SCHEDULE.getKey(), "1000ms" )
                .put(TransportSettings.BIND_HOST.getKey(), host)
                .put(TransportSettings.PUBLISH_HOST.getKey(), host)
                .put(TransportSettings.PORT.getKey(), port)
                .build();

        ThreadPool threadPool = new ThreadPool(builder()
                .put(Node.NODE_NAME_SETTING.getKey(), this.getClass().getName() )
                .build()) ;

        Netty4Transport netty4Transport = new Netty4Transport(clientSettings, Version.CURRENT, threadPool,
                new NetworkService(Collections.emptyList()), new PageCacheRecycler(clientSettings),
                new NamedWriteableRegistry(Collections.emptyList()), new NoneCircuitBreakerService());

        return netty4Transport;
    }

    @Singleton
    @Provides
    @Named(HTTP_SERVER)
    private LifecycleComponent createNettyHttpServer(Props props, HttpHandler handler) {
        return new NettyHttpServer(props, handler);
    }
}
