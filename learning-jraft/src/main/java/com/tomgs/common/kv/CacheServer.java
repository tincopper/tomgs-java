package com.tomgs.common.kv;

/**
 * CacheServer
 *
 * @author tomgs
 * @since 2022/3/22
 */
public interface CacheServer {

    void start() throws Exception;

    void close() throws Exception;

}
