package com.tomgs.ratis.kv.protocol;

/**
 * CmdType
 *
 * @author tomgs
 * @since 2022/3/24
 */
public enum CmdType {
    GET,
    PUT,
    DELETE,
    WATCH,
    UNWATCH,
    BPOP
}
