package com.tomgs.es.gateway.transport.http.netty4.v2;

import com.tomgs.es.gateway.transport.ProxyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tomgs.es.gateway.transport.http.netty4.v2.DefaultHttpHandler.CLIENT_CHANNEL_KEY;

/**
 * @author tangzhongyuan
 * @since 2019-05-06 17:11
 **/
public class HttpProxyBackendHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HttpProxyBackendHandler.class);

    //private Channel inboundChannel;
    //private final GatewayMetrics metrics;
    //private final ESRestRequest request;
    //private final HttpHandlerContent content;

    public HttpProxyBackendHandler() {
        //this.metrics = metrics;
        //this.content = content;
        //this.request = content.getAttribute(Constants.ES_REST_REQUEST);
        //this.inboundChannel = content.getClientChannel();
    }

    ConnectionPoolManager manager;

    public HttpProxyBackendHandler(ConnectionPoolManager manager) {
        this.manager = manager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /*ctx.channel().writeAndFlush(content.getRequest()).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                ctx.read();
                content.getClientChannel().read();
                //ESRestRequest restRequest = content.getAttribute(Constants.ES_REST_REQUEST);
                //metrics.markIndexQps(restRequest.getIndexName());
                //if (OperateTypeEnum.isWriteType(restRequest.getOperateType())) {
                //    metrics.markIndexWriterSize(restRequest.getIndexName(), restRequest.getRequestSize());
                //}
            } else {
                log.error("channelId:[{}], send request to es node failure, msg:[{}]",
                        ProxyUtil.getChannelId(ctx), future.cause().getMessage(), future.cause());
                ProxyUtil.closeOnFlush(inboundChannel,
                        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, GateWayResponseStatus.ES_NODE_REQUEST_ERROR));
            }
        });*/
        ctx.read();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            final int dataSize = response.content().readableBytes();
            final Attribute<HttpHandlerContent> attr = ctx.channel().attr(CLIENT_CHANNEL_KEY);
            final HttpHandlerContent httpHandlerContent = attr.get();
            final Channel inboundChannel = httpHandlerContent.getClientChannel();
            //响应客户端不做修改
            inboundChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    ctx.channel().read();
                    inboundChannel.read();
                    //if (OperateTypeEnum.isReadType(request.getOperateType())) {
                    //    metrics.markIndexReaderSize(request.getIndexName(), dataSize);
                    //}
                } else {
                    log.error("channelId:[{}], send response to client failure, msg:[{}]",
                            ProxyUtil.getChannelId(ctx), future.cause().getMessage(), future.cause());
                    ProxyUtil.closeOnFlush(inboundChannel,
                            new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, GateWayResponseStatus.PROXY_RESPONSE_ERROR));
                }
            });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Attribute<HttpHandlerContent> attr = ctx.channel().attr(CLIENT_CHANNEL_KEY);
        final HttpHandlerContent httpHandlerContent = attr.get();
        final Channel inboundChannel = httpHandlerContent.getClientChannel();
        if (inboundChannel.isActive()) {
            ProxyUtil.closeOnFlush(inboundChannel);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        manager.release(ctx.channel());
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("HttpProxyBackendHandler catch exception, channelId:[{}], msg:[{}].",
                ProxyUtil.getChannelId(ctx), cause.getMessage(), cause);
        ProxyUtil.closeOnFlush(ctx.channel());
    }
}
