package com.tomgs.es.gateway.transport.http.netty4.v2;

import com.tomgs.es.gateway.common.TimeUtil;
import com.tomgs.es.gateway.transport.ProxyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tangzhongyuan
 * @since 2019-05-06 16:51
 **/
public class HttpProxyFrontendHandlerV2 extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HttpProxyFrontendHandlerV2.class);

    private final HttpHandler httpHandler;

    public HttpProxyFrontendHandlerV2(final HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        httpHandler.preDispatcherHandler(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //解析http请求，构造http请求，转发请求
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            /*final FullHttpRequest copyRequest = new DefaultFullHttpRequest(
                    request.protocolVersion(),
                    request.method(),
                    request.uri(),
                    Unpooled.copiedBuffer(request.content()),
                    request.headers(),
                    request.trailingHeaders());*/

            dispatchRequest(request, ctx.channel());
        } else {
            //记录为非法请求
            log.warn("Illegal request, msg:[{}]", msg);
        }

    }

    private void dispatchRequest(FullHttpRequest request, Channel channel) {
        final int dataSize = request.content().readableBytes();
        HttpHandlerContent requestContent = new HttpHandlerContent(request, channel, dataSize);
        final long startTime = TimeUtil.currentTimeMillis();
        httpHandler.dispatcherHandler(requestContent);
        log.info("REQUEST ## dispatcher handler cost time:[{}]", TimeUtil.currentTimeMillis() - startTime);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        httpHandler.postDispatcherHandler(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("HttpProxyFrontendHandler catch exception, channelId:[{}], msg:[{}].",
                ProxyUtil.getChannelId(ctx), cause.getMessage(), cause);
        ProxyUtil.closeOnFlush(ctx.channel());
    }

}
