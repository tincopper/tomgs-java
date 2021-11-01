package com.tomgs.learning.dubbo.client.demo1;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.tomgs.learning.dubbo.api.Helloworld;

/**
 * JsonTest
 *
 * @author tomgs
 * @since 2021/11/1
 */
public class JsonTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Helloworld.HelloRequest request = Helloworld.HelloRequest.newBuilder().setName("tomgs").build();
        String print = JsonFormat.printer().print(request);
        System.out.println(print);

        Helloworld.HelloRequest.Builder builder = Helloworld.HelloRequest.newBuilder();
        JsonFormat.parser().merge(print, builder);
        System.out.println(builder);
    }

}
