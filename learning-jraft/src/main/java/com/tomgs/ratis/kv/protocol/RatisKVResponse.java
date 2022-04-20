package com.tomgs.ratis.kv.protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * RatisKVResponse
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Data
@Builder
public class RatisKVResponse implements Serializable {

    private CmdType cmdType;

    private Long requestId;

    private Long traceId;

    private Boolean success;

    private String message;

    // 详细状态，状态码
    // private Status status;

    private GetResponse getResponse;

    private PutResponse putResponse;

    private BPopResponse bPopResponse;

}
