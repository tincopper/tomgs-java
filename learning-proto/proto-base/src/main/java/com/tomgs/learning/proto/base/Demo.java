package com.tomgs.learning.proto.base;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.tomgs.learning.dubbo.api.HelloRequest;
import com.tomgs.learning.proto.api.HelloOptionProto;
import com.tomgs.learning.proto.api.MyMessage;

/**
 * Demo
 *
 * @author tomgs
 * @since 2021/11/1
 */
public class Demo {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        HelloRequest request = HelloRequest.newBuilder().setName("tomgs").build();
        String print = JsonFormat.printer().print(request);
        System.out.println(print);

        HelloRequest.Builder builder = HelloRequest.newBuilder();
        JsonFormat.parser().merge(print, builder);
        System.out.println(builder);

        System.out.println(HelloOptionProto.myOption);

        String value = MyMessage.getDescriptor().getOptions()
                .getExtension(HelloOptionProto.myOption);
        System.out.println(value);
    }

}
