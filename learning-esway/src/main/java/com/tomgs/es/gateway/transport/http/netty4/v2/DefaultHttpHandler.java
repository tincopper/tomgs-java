package com.tomgs.es.gateway.transport.http.netty4.v2;

import com.tomgs.es.gateway.common.HttpUtils;
import com.tomgs.es.gateway.common.ServerInfo;
import com.tomgs.es.gateway.common.component.filter.Invoker;
import com.tomgs.es.gateway.common.exception.FilterException;
import com.tomgs.es.gateway.transport.ProxyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class DefaultHttpHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpHandler.class);
    private static final AttributeKey<Channel> PROXY_CHANNEL_KEY = AttributeKey.valueOf("netty.proxy.channel");
    public static final AttributeKey<HttpHandlerContent> CLIENT_CHANNEL_KEY = AttributeKey.valueOf("netty.client.channel");
    private static final ConnectionPoolManager manager = ConnectionPoolManager.INSTANCE;
    //private final FilterFacade<HttpHandlerContent> filterFacade;
    //private final ESNodeManager nodeManager;
    //private final GatewayMetrics metrics;
    private final int maxRetry = 3;
    final EsRequestInvoker requestInvoker;

    public DefaultHttpHandler() {
        requestInvoker = new EsRequestInvoker();
        //final EsRequestInvoker requestInvoker = new EsRequestInvoker();
        //final List<Filter> filters = FilterFactory.getFilterExtension();
        //this.filterFacade = new FilterFacade<>(requestInvoker, filters);
        //this.nodeManager = ServiceProvider.INSTANCE.getInstance(ESNodeManager.class);
        //this.metrics = ServiceProvider.INSTANCE.getInstance(GatewayMetrics.class);
    }

    @Override
    public void preDispatcherHandler(Channel channel) {
        if (channel != null) {
            channel.read();
        }
    }

    @Override
    public void dispatcherHandler(HttpHandlerContent requestContent) {
        try {
            //ESRestRequest request = ESRestRequest.build(requestContent);
            //assert request != null : "es request must be not null";
            //logger.info(buildRequestLogInfo(requestContent.getRequest(), request));
            //validate(request);
            //requestContent.attribute(Constants.ES_REST_REQUEST, request);
            //requestContent.attribute(Constants.INDEX_NAME, request.getIndexName());
            //filterFacade.filter(requestContent);
            requestInvoker.invoker(requestContent);
        } catch (Throwable t) {
            logger.error("request failure, msg:[{}]", t.getMessage(), t);
            if (requestContent.getResponse() != null) {
                requestContent.response();
            } else {
                requestContent.response(HttpUtils.INSTANCE
                        .createFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY, t.getMessage()));
            }
        }
    }

    @Override
    public void postDispatcherHandler(Channel channel) {
        Attribute<Channel> proxyChannelAttr = channel.attr(PROXY_CHANNEL_KEY);
        Channel proxyChannel = proxyChannelAttr.get();
        if (proxyChannel != null && proxyChannel.isActive()) {
            ProxyUtil.closeOnFlush(proxyChannel);
        }
    }


    /**
     * this real to es server
     */
    class EsRequestInvoker implements Invoker<HttpHandlerContent> {

        private final Logger log = LoggerFactory.getLogger(DefaultHttpHandler.class);

        @Override
        public void invoker(HttpHandlerContent content) throws FilterException {
            //sendRequestToEsNode(content);

            InetSocketAddress address = new InetSocketAddress("10.4.4.202", 9200);
            final ChannelPool channelPool = manager.getChannel(address);
            channelPool.acquire().addListener((FutureListener<Channel>) future -> {
                if (future.isSuccess()) {
                    Channel channel = future.getNow();
                    channel.attr(CLIENT_CHANNEL_KEY).set(content);
                    channel.writeAndFlush(content.getRequest());
                    // Release back to pool
                    //channelPool.release(channel);
                }
            });

        }

        private void sendRequestToEsNode(HttpHandlerContent content) {
            Channel inboundChannel = content.getClientChannel();
            final Channel proxyChannel = inboundChannel.attr(PROXY_CHANNEL_KEY).get();
            if (proxyChannel != null && proxyChannel.isActive()) {
                sendRequest(content, inboundChannel, proxyChannel);
                return;
            }
            Bootstrap bootstrap = buildBootstrap(content, inboundChannel);
            connectAndSendRequest(bootstrap, inboundChannel, maxRetry);
        }

        private Bootstrap buildBootstrap(HttpHandlerContent content, Channel inboundChannel) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(inboundChannel.eventLoop()).channel(inboundChannel.getClass())
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.AUTO_READ, false)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //new LoggingHandler(LogLevel.INFO),
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(65536),
                                    new HttpProxyBackendHandler());
                        }
                    });
            return bootstrap;
        }

        private void sendRequest(HttpHandlerContent content, Channel inboundChannel, Channel proxyChannel) {
            proxyChannel.writeAndFlush(content.getRequest()).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    proxyChannel.read();
                    inboundChannel.read();
                    //ESRestRequest restRequest = content.getAttribute(Constants.ES_REST_REQUEST);
                    //metrics.markIndexQps(restRequest.getIndexName());
                    //if (OperateTypeEnum.isWriteType(restRequest.getOperateType())) {
                    //    metrics.markIndexWriterSize(restRequest.getIndexName(), restRequest.getRequestSize());
                    //}
                } else {
                    log.error("channelId:[{}], send request to es node failure, msg:[{}]",
                            proxyChannel.id().asShortText(), future.cause().getMessage(), future.cause());
                    ProxyUtil.closeOnFlush(inboundChannel,
                            new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, GateWayResponseStatus.ES_NODE_REQUEST_ERROR));
                }
            });
        }

        private void connectAndSendRequest(Bootstrap bootstrap, Channel inboundChannel, int retry) {
            final ServerInfo serverInfo = selectServerInfo();
            bootstrap.connect(serverInfo.getHost(), serverInfo.getHttpPort())
                    .addListener((ChannelFutureListener) channelFuture -> {
                        if (channelFuture.isSuccess()) {
                            log.info("http proxy connectAndSendRequest to es node [{}:{}] success.",
                                    serverInfo.getHost(), serverInfo.getHttpPort());
                            Attribute<Channel> proxyChannelAttribute
                                    = inboundChannel.attr(PROXY_CHANNEL_KEY);
                            proxyChannelAttribute.set(channelFuture.channel());
                        } else if (retry == 0) {
                            log.error("http proxy retries connection es node [{}:{}] [{}] times failed.",
                                    serverInfo.getHost(), serverInfo.getHttpPort(), maxRetry);
                            //response connectAndSendRequest failure to client
                            ProxyUtil.closeOnFlush(inboundChannel,
                                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, GateWayResponseStatus.ES_CONNECT_REFUSED));
                        } else {
                            int order = (maxRetry - retry) + 1;
                            int delay = 1 << order;
                            log.warn("http proxy retry connection es node [{}:{}] for the [{}th] time",
                                    serverInfo.getHost(), serverInfo.getHttpPort(), order);
                            bootstrap.config().group().schedule(() -> connectAndSendRequest(bootstrap, inboundChannel, retry - 1),
                                    delay, TimeUnit.SECONDS);
                        }
                    });
        }

        private ServerInfo selectServerInfo() {
            return null;
        }
    }

}
