package com.tomgs.learning.grpc.exception;

/**
 * GrpcInitException
 *
 * @author tomgs
 * @since 2022/4/25
 */
public class GrpcInitException extends RuntimeException {

    public GrpcInitException(String message) {
        super(message);
    }

    public GrpcInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrpcInitException(Throwable cause) {
        super(cause);
    }

}
