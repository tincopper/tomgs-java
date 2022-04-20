package com.tomgs.ratis.kv.protocol;

import lombok.Data;

/**
 * WatchRequest
 *
 * @author tomgs
 * @since 2022/4/19
 */
@Data
public class WatchRequest {

    private byte[] key;

}
