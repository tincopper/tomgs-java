package com.tomgs.ratis.kv.protocol.ext;

import com.tomgs.ratis.kv.protocol.CmdType;

import java.io.Serializable;

/**
 * BaseRequest
 *
 * @author tomgs
 * @since 2022/3/24
 */
public abstract class BaseRequest implements Serializable {

    public abstract CmdType type();

}
