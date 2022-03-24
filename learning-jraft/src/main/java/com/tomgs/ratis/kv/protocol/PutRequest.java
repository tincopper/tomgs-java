package com.tomgs.ratis.kv.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * GetRequest
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Data
public class PutRequest implements Serializable {

    private byte[] key;

    private byte[] value;

}
