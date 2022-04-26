package com.tomgs.learning.grpc.core;

import io.grpc.BindableService;

import java.util.List;

/**
 * GrpcServerInit
 *
 * @author tomgs
 * @since 2022/4/25
 */
public interface GrpcServerInit {

    /**
     * grpc服务端口
     * @return 端口号，默认50051
     */
    default Integer serverPort() {
        return 50051;
    }

    /**
     * grpc服务提供实例
     * @return 暴露的服务实例
     */
    List<BindableService> serviceProvider();

}
