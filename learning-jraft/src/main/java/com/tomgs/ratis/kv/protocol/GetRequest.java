package com.tomgs.ratis.kv.protocol;

import lombok.Data;

/**
 * GetRequest
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Data
public class GetRequest {

    private byte[] key;

}
