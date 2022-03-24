package com.tomgs.ratis.kv.exception;

/**
 * RatisKVClientException
 *
 * @author tomgs
 * @since 2022/3/24
 */
public class RatisKVClientException extends RuntimeException {

    public RatisKVClientException(String message) {
        super(message);
    }

    public RatisKVClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
