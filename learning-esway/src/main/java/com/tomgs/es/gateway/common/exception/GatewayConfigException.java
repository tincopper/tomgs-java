package com.tomgs.es.gateway.common.exception;

/**
 * es gateway server exception
 *
 * @author tangzhongyuan
 * @since 2019-05-08 14:38
 **/
public class GatewayConfigException extends RuntimeException {

    public GatewayConfigException(String message) {
        super(message);
    }

    public GatewayConfigException(Throwable cause) {
        super(cause);
    }

    public GatewayConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
