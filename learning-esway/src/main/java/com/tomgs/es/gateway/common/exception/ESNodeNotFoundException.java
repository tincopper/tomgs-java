package com.tomgs.es.gateway.common.exception;

/**
 * @author tangzhongyuan
 * @since 2019-06-12 20:37
 **/
public class ESNodeNotFoundException extends RuntimeException {

    public ESNodeNotFoundException(String message) {
        super(message);
    }

    public ESNodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public ESNodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
