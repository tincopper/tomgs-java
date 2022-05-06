package com.tomgs.learning.grpc.core;

import com.tomgs.learning.grpc.exception.GrpcInitException;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * GrpcStartService
 *
 * @author tomgs
 * @since 2022/4/25
 */
@Slf4j
public class GrpcStartService implements Service {

    private List<Server> servers;

    private boolean isStarted = false;

    private final ExecutorService executor = ThreadUtils.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2, "grpc-exec");

    @Override
    public String getName() {
        return "grpcStartService";
    }

    @Override
    public void start() {
        log.info("grpc server start.");
        servers = new ArrayList<>();
        ServiceLoader<GrpcServerInit> serviceLoader = ServiceLoader.load(GrpcServerInit.class);
        ServiceLoader<ServerInterceptor> serverInterceptorLoader = ServiceLoader.load(ServerInterceptor.class);
        ServiceLoader<ServerTransportFilter> serverTransportFilterLoader = ServiceLoader.load(ServerTransportFilter.class);
        for (GrpcServerInit serverInit : serviceLoader) {
            Integer serverPort = serverInit.serverPort();
            List<BindableService> bindableServices = serverInit.serviceProvider();
            ServerBuilder<?> serverBuilder = ServerBuilder.forPort(serverPort);
            for (ServerInterceptor serverInterceptor : serverInterceptorLoader) {
                serverBuilder.intercept(serverInterceptor);
            }
            for (ServerTransportFilter serverTransportFilter : serverTransportFilterLoader) {
                serverBuilder.addTransportFilter(serverTransportFilter);
            }

            serverBuilder.executor(executor);
            bindableServices.forEach(serverBuilder::addService);
            try {
                Server server = serverBuilder.build().start();
                servers.add(server);
            } catch (IOException e) {
                throw new GrpcInitException("启动GRPC服务异常: " + e.getMessage(), e);
            }
        }
        isStarted = true;
    }

    @Override
    public void stop() {
        log.info("grpc server stop.");
        executor.shutdown();

        if (servers != null) {
            try {
                for (Server server : servers) {
                    server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                throw new GrpcInitException("关闭GRPC服务异常: " + e.getMessage(), e);
            }
        }
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

}
