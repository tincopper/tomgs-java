package com.tomgs.ratis.kv.protocol;

import com.tomgs.ratis.kv.watch.DataChangeEvent;
import lombok.Data;

/**
 * BPopResponse
 *
 * @author tomgs
 * @since 2022/4/19
 */
@Data
public class BPopResponse {

    private CmdType cmdType;

    private DataChangeEvent event;
}
