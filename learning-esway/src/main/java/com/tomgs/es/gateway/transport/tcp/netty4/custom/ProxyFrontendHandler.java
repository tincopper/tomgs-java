package com.tomgs.es.gateway.transport.tcp.netty4.custom;

import com.tomgs.es.gateway.transport.InboundMessage;
import com.tomgs.es.gateway.transport.tcp.netty4.Netty4Utils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import org.elasticsearch.Version;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.compress.Compressor;
import org.elasticsearch.common.compress.CompressorFactory;
import org.elasticsearch.common.compress.NotCompressedException;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ThreadContext;
import org.elasticsearch.core.internal.io.IOUtils;
import org.elasticsearch.node.Node;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TcpHeader;
import org.elasticsearch.transport.TransportStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;

import static org.elasticsearch.common.settings.Settings.builder;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 14:26
 **/
public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    private String realHost = "10.18.4.23";
    private int realPort = 9300;

    private Channel outboundChannel;
    private final InboundMessage.Reader reader;
    private final ThreadPool threadPool;

    public ProxyFrontendHandler() {
        threadPool = new ThreadPool(builder()
                .put(Node.NODE_NAME_SETTING.getKey(), this.getClass().getName())
                .build());

        reader = new InboundMessage.Reader(Version.CURRENT,
                new NamedWriteableRegistry(Collections.emptyList()), threadPool.getThreadContext());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        Channel inbound = ctx.channel();
        bootstrap.group(inbound.eventLoop()).channel(inbound.getClass());
        bootstrap.option(ChannelOption.AUTO_READ, false);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProxyBackendHandler(inbound));
            }
        });

        ChannelFuture future = bootstrap.connect(realHost, realPort);

        outboundChannel = future.channel();
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                inbound.read();
            } else {
                inbound.close();
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("代理连接断开...");
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }

    /**
     * 在这里接收客户端的消息
     * 在客户端和代理服务器建立连接时，也获得了代理服务器和目标服务器的通道outbound，
     * 通过outbound写入消息到目标服务器
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("from client message:" + msg);

        //Transports.assertTransportThread();
        assert msg instanceof ByteBuf : "Expected message type ByteBuf, found: " + msg.getClass();

        final ByteBuf buffer = (ByteBuf) msg;
        try {
            InetSocketAddress remoteAddress = (InetSocketAddress)outboundChannel.remoteAddress();
            ThreadContext threadContext = threadPool.getThreadContext();
            BytesReference reference = Netty4Utils.toBytesReference(buffer);

            String logMessage = logMessage(reference);
            System.out.println(logMessage);

            InboundMessage message = reader.deserialize(reference);
            message.getStoredContext().restore();
            threadContext.putTransient("_remote_address", remoteAddress);

            if (message.isRequest()) {
                InboundMessage.RequestMessage requestMessage = (InboundMessage.RequestMessage) message;
                final Set<String> features = requestMessage.getFeatures();
                final String action = requestMessage.getActionName();
                final long requestId = requestMessage.getRequestId();
                final StreamInput stream = requestMessage.getStreamInput();
                final Version version = requestMessage.getVersion();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            buffer.release();
        }

        if (outboundChannel.isActive()) {
            sendRequest(ctx, msg);
        }
    }

    private void sendRequest(ChannelHandlerContext ctx, Object msg) {
        outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // was able to flush out data, start to read the next chunk
                    ctx.channel().read();
                } else {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static final int HEADER_SIZE = TcpHeader.MARKER_BYTES_SIZE + TcpHeader.MESSAGE_LENGTH_SIZE;
    public String logMessage(BytesReference message) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int messageLengthWithHeader = HEADER_SIZE + message.length();
        boolean success = false;
        StreamInput streamInput = message.streamInput();
        try {
            final long requestId = streamInput.readLong();
            final byte status = streamInput.readByte();
            final boolean isRequest = TransportStatus.isRequest(status);
            final String type = isRequest ? "request" : "response";
            final String version = Version.fromId(streamInput.readInt()).toString();
            sb.append(" [length: ").append(messageLengthWithHeader);
            sb.append(", request id: ").append(requestId);
            sb.append(", type: ").append(type);
            sb.append(", version: ").append(version);

            if (isRequest) {
                if (TransportStatus.isCompress(status)) {
                    Compressor compressor;
                    try {
                        final int bytesConsumed = TcpHeader.REQUEST_ID_SIZE + TcpHeader.STATUS_SIZE + TcpHeader.VERSION_ID_SIZE;
                        compressor = CompressorFactory.compressor(message.slice(bytesConsumed, message.length() - bytesConsumed));
                    } catch (NotCompressedException ex) {
                        throw new IllegalStateException(ex);
                    }
                    streamInput = compressor.streamInput(streamInput);
                }

                try (ThreadContext context = new ThreadContext(Settings.EMPTY)) {
                    context.readHeaders(streamInput);
                }
                // now we decode the features
                if (streamInput.getVersion().onOrAfter(Version.V_6_3_0)) {
                    streamInput.readStringArray();
                }
                sb.append(", action: ").append(streamInput.readString());
            }
            sb.append(']');
            sb.append(' ').append("event").append(": ").append(messageLengthWithHeader).append('B');
            success = true;
        } finally {
            if (success) {
                IOUtils.close(streamInput);
            } else {
                IOUtils.closeWhileHandlingException(streamInput);
            }
        }

        return sb.toString();
    }
}
