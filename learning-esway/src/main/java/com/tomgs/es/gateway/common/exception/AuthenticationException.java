package com.tomgs.es.gateway.common.exception;

import org.apache.http.ProtocolException;

/**
 * @author tangzhongyuan
 * @since 2019-06-05 21:51
 **/
public class AuthenticationException extends ProtocolException {

    private static final long serialVersionUID = -6794031905674764776L;

    /**
     * Creates a new AuthenticationException with a {@code null} detail message.
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Creates a new AuthenticationException with the specified message.
     *
     * @param message the exception detail message
     */
    public AuthenticationException(final String message) {
        super(message);
    }

    /**
     * Creates a new AuthenticationException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     * if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
