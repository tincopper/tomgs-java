package com.tomgs.learning.proto.base;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.tomgs.learning.cinema.Cinema;
import org.junit.Test;
import tomgs.api.ExtDemo;

/**
 * Demo
 *
 * @author tomgs
 * @since 2021/11/1
 */
public class JsonPrintDemo {

    @Test
    public void testBase() throws InvalidProtocolBufferException {
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

        // pb对象序列化为json字符串
        String print1 = PBJsonFormat.printer().print(request);
        System.out.println(print1);

        // json字符串反序列化为pb对象
        ExtDemo.HelloRequest.Builder builder1 = ExtDemo.HelloRequest.newBuilder();
        PBJsonFormat.parser().merge(print1, builder1);
        System.out.println(builder1);

        // pb对象序列化为指定格式的json字符串
        String print2 = PBJsonFormat.innerPrinter().print(request);
        System.out.println(print2);

        // 指定格式的json字符串反序列化pb对象
        ExtDemo.HelloRequest.Builder builder2 = ExtDemo.HelloRequest.newBuilder();
        PBJsonFormat.parser().ignoringUnknownFields().merge(print2, builder2);
        System.out.println(builder2);

        ExtDemo.User user = ExtDemo.User.newBuilder()
                .setName("test")
                .setAge(12)
                .setCardNumber(78912356789L)
                .setSalary(78912356789L)
                .setId("123")
                .build();
        String print3 = PBJsonFormat.printer().print(user);
        System.out.println(print3);

        String print33 = JsonFormat.printer().print(user);
        System.out.println(print3);

        String print4 = PBJsonFormat.innerPrinter().print(user);
        System.out.println(print4);

        ExtDemo.User.Builder builder3 = ExtDemo.User.newBuilder();
        PBJsonFormat.parser().ignoringUnknownFields().merge(print3, builder3);
        System.out.println(builder3.build());
    }

    @Test
    public void testJsonArray() throws InvalidProtocolBufferException {
        final Cinema.Movie movie1 = Cinema.Movie.newBuilder().setName("test1").setAddress("123").setDescription("345354").build();
        final Cinema.Movie movie2 = Cinema.Movie.newBuilder().setName("test2").setAddress("123").setDescription("345354").build();
        Cinema.Ticket.Builder builder = Cinema.Ticket.newBuilder();
        // 默认值不会在json输出
        builder.setId(2).addMovie(movie1).addMovie(movie2);

        final Cinema.Ticket ticket = builder.build();
        String print4 = JsonFormat.printer().print(ticket);
        System.out.println(print4);

        Cinema.Ticket.Builder builder2 = Cinema.Ticket.newBuilder();
        JsonFormat.parser().merge(print4, builder2);
        System.out.println(builder2.build());
    }

}
