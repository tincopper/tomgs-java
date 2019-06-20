package com.tomgs.netty.nettypool.demo3;

import io.netty.channel.Channel;
import lombok.Builder;

/**
 * @author tangzhongyuan
 * @since 2019-06-17 17:55
 **/
@Builder
public class CustomerChannel {

    private int id;

    private Channel channel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
