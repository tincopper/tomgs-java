package com.tomgs.ratis.kv.protocol;

import lombok.Data;

/**
 * UnwatchRequest
 *
 * @author tomgs
 * @since 2022/4/19
 */
@Data
public class UnwatchRequest {

    private byte[] key;

}
