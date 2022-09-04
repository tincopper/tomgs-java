package com.tomgs.learning.proto.base;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import tomgs.api.ExtDemo;

/**
 * Demo
 *
 * @author tomgs
 * @since 2021/11/1
 */
public class JsonPrintDemo {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        // proto 消息对象为下划线会转换为驼峰的命名方式
        ExtDemo.HelloRequest request = ExtDemo.HelloRequest.newBuilder().setTestName("tomgs").build();
        String print = JsonFormat.printer().print(request);
        System.out.println(print);

        ExtDemo.HelloRequest.Builder builder = ExtDemo.HelloRequest.newBuilder();
        JsonFormat.parser().merge(print, builder);
        System.out.println(builder);

        /*
        JsonFormat.TypeRegistry typeRegistry = JsonFormat.TypeRegistry.newBuilder()
                .add(FiledProto.getDescriptor().getMessageTypes())
                .build();
        JsonFormat.Printer printer = JsonFormat.printer().usingTypeRegistry(typeRegistry);
        String inner_name = printer.print(request);
        System.out.println(inner_name);
        */

        String print1 = PBJsonFormat.printer().print(request);
        System.out.println(print1);

        ExtDemo.HelloRequest.Builder builder1 = ExtDemo.HelloRequest.newBuilder();
        PBJsonFormat.parser().merge(print1, builder1);
        System.out.println(builder1);

        String print2 = PBJsonFormat.innerPrinter().print(request);
        System.out.println(print2);

        ExtDemo.HelloRequest.Builder builder2 = ExtDemo.HelloRequest.newBuilder();
        PBJsonFormat.parser().merge(print2, builder2);
        System.out.println(builder2);
    }

}
