package com.tomgs.learning.grpc;

import com.tomgs.learning.grpc.core.GrpcServerInit;
import io.grpc.BindableService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GrpcServiceRegistry
 *
 * @author tomgs
 * @since 2022/4/25
 */
public class GrpcServiceRegistry implements GrpcServerInit {

    private static final Set<BindableService> services = new HashSet<>();

    public GrpcServiceRegistry addService(BindableService service) {
        services.add(service);
        return this;
    }

    @Override
    public Integer serverPort() {
        return 50051;
    }

    @Override
    public List<BindableService> serviceProvider() {
        return new ArrayList<>(services);
    }

}
