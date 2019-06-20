package com.tomgs.es.gateway.transport;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


public class ProxyUtil {

    private static final Logger log = LoggerFactory.getLogger(ProxyUtil.class);

    public static boolean writeAndFlush(ChannelHandlerContext ctx, Object msg, boolean isCloseOnError) {
        if (ctx.channel().isActive()) {
            ctx.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess())
                    log.info("channelId:[{}], send request success.", getChannelId(ctx));
                else
                    log.error("channelId:[{}], send request failure, msg:[{}].",
                            getChannelId(ctx), future.cause().getMessage(), future.cause());
            });
            return true;
        } else if (isCloseOnError)
            ctx.close();
        return false;
    }

    public static void closeOnFlush(Channel ch, Object msg) {
        ch.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    public static void closeOnFlush(Channel ch) {
        ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public static void closeChannel(Channel ch) {
        if (ch != null) {
            ch.close();
        }
    }

    /**
     * 从channel中解析出客户端ip
     */
    public static String getIpByChannel(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    /**
     * 从request中获取 客户端请求的目标服务器的 ip和port
     */
    public static InetSocketAddress getAddressByRequest(FullHttpRequest request) {
        String[] temp1 = request.headers().get("host").split(":");
        return new InetSocketAddress(temp1[0], temp1.length == 1 ? 80 : Integer.parseInt(temp1[1]));
    }

    /**
     * 从ctx中获取到当前通道的id
     */
    public static String getChannelId(ChannelHandlerContext ctx) {
        return ctx.channel().id().asShortText();
    }

    /**
     * 使用ctx给客户端发送失败响应,默认就为请求超时
     */
    public static void responseFailedToClient(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_TIMEOUT));
    }

}
