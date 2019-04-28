package com.tomgs.es.gateway.transport.http.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 17:11
 **/
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        //100 Continue
        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.CONTINUE));
        }

        // 获取请求的uri
        String uri = req.uri();
        String methodName = req.method().name();
        HttpHeaders headers = req.headers();

        ByteBuf buf = req.content();
        byte[] bytes = new byte[buf.readableBytes()];
        //将信息读到字节数组
        buf.readBytes(bytes);
        String content = new String(bytes, "UTF-8");

        System.out.println(content);

        //发送代理请求
        String host = "127.0.0.1";
        int port = 9200;
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, "http"));
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        //设置每个请求需要发送的默认headers，这样就不用在每个请求中指定它们。
        builder.setDefaultHeaders(defaultHeaders);
        // 设置应该授予的超时时间，以防对相同的请求进行多次尝试。默认值是30秒，与默认socket超时时间相同。
        // 如果自定义socket超时时间，则应相应地调整最大重试超时时间。
        builder.setMaxRetryTimeoutMillis(100000);

        RestClient restClient = builder.build();
        System.out.println("连接" + host + ":" + port + "成功...");

        Request request = new Request(methodName, uri);
        request.setJsonEntity(content);
        //request.
        Response realResponse = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(realResponse.getEntity());

        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(responseBody, CharsetUtil.UTF_8));
        // 设置头信息
        for (Header header : realResponse.getHeaders()) {
            response.headers().set(header.getName(), header.getValue());
        }
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        // 将html write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
