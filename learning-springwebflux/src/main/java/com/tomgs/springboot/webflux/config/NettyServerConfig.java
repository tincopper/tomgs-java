package com.tomgs.springboot.webflux.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;

/**
 * @author tomgs
 * @since 2021/1/28
 */
@Configuration
public class NettyServerConfig {

  @Value("${server.port}")
  private int port;

  @Value("${server.address}")
  private String host;

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Bean
  public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
    NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory() {
      @Override
      public WebServer getWebServer(HttpHandler httpHandler) {
        Map<String, HttpHandler> handlerMap = new HashMap<>();
        handlerMap.put(contextPath, httpHandler);
        return super.getWebServer(new ContextPathCompositeHandler(handlerMap));
      }
    };
    webServerFactory.addServerCustomizers(serverCustomizer());
    return webServerFactory;
  }

  public NettyServerCustomizer serverCustomizer() {
    System.out.println(String.format("web server host:%s, port:%s", host, port));
    return httpServer -> httpServer.host(host).port(port);
  }

}
