package com.tomgs.ratis.kv.protocol.ext;

import com.tomgs.ratis.kv.protocol.CmdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GetRequest
 *
 * @author tomgs
 * @since 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetRequest extends BaseRequest {

    private byte[] key;

    @Override
    public CmdType type() {
        return CmdType.GET;
    }

}
