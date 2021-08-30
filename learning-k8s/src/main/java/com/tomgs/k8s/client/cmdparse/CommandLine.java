package com.tomgs.k8s.client.cmdparse;

import io.fabric8.kubernetes.client.dsl.MixedOperation;

/**
 * @author tomgs
 * @since 2021/8/25
 */
public class CommandLine {

    private MixedOperation<?, ?, ?> operation;

    public Object execute() {
        return operation.inNamespace("").withName("");
    }

}
