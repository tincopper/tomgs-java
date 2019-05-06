package com.tomgs.netty.demo1;

import io.netty.channel.*;

/**
 * @author tangzhongyuan
 * @since 2019-05-05 18:36
 **/
public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel inboundChannel;

    public ProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    //当和目标服务器的通道连接建立时
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>>>>>>>>>ACTIVE>>>>>>>>>>");
        //一定要加上，不然客户端收不到请求
        ctx.read();
    }


    /**
     * msg是从目标服务器返回的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("## Target server returns data:" + msg.toString());
        /**
         * 接收目标服务器发送来的数据并打印
         * 然后把数据写入代理服务器和客户端的通道里
         */
        //通过inboundChannel向客户端写入数据
        System.out.println("## After the proxy server receives a response to the target server to the client to say: I am a proxy server, the target server to replace him, I say to you, thank you. ");
        String resDataToClient = "I am a proxy server, the target server to replace him, I say to you, thank you.";
        inboundChannel.writeAndFlush(resDataToClient).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    inboundChannel.close();
                } else {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>>>>>>>>>IN-ACTIVE>>>>>>>>>>");
        ProxyFrontendHandler.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ProxyFrontendHandler.closeOnFlush(ctx.channel());
    }
}
