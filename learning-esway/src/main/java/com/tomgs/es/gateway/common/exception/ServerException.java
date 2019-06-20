package com.tomgs.es.gateway.common.exception;

/**
 * es gateway server exception
 *
 * @author tangzhongyuan
 * @since 2019-05-08 14:38
 **/
public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
