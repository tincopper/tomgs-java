package com.tomgs.ratis.kv.protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * GetResponse
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Data
@Builder
public class GetResponse implements Serializable {

    private byte[] value;

}
