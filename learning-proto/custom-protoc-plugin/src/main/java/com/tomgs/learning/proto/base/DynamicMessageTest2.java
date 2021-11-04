package com.tomgs.learning.proto.base;

import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import com.tomgs.learning.cinema.Cinema;
import com.tomgs.learning.selfmd.Selfmd;

import java.io.FileInputStream;

/**
 * DynamicMessageTest
 *
 * @author tomgs
 * @since 2021/11/2
 */
public class DynamicMessageTest2 {

    public static void main(String[] args) throws Exception {
        String source = System.getProperty("user.dir") + "/learning-proto/custom-protoc-plugin/src/main/proto/";

        Cinema.Ticket initMsg = initMsg();
        System.out.println(initMsg);

        //product
        System.out.println("--------------protuct--------------");
        DescriptorProtos.FileDescriptorSet descriptorSet = DescriptorProtos.FileDescriptorSet
                .parseFrom(new FileInputStream(source + "/cinema.desc"));

        Selfmd.SelfDescribingMessage.Builder selfmdBuilder = Selfmd.SelfDescribingMessage.newBuilder();
        selfmdBuilder.setDescriptorSet(descriptorSet);
        selfmdBuilder.setMsgName(Cinema.Ticket.getDescriptor().getFullName());
        selfmdBuilder.setMessage(initMsg.toByteString());
        Selfmd.SelfDescribingMessage build = selfmdBuilder.build();
        System.out.println("自描述信息：" + build);
        byte[] byteArray = build.toByteArray();

        //customer
        System.out.println("--------------customer--------------");
        Selfmd.SelfDescribingMessage parseFrom = Selfmd.SelfDescribingMessage.parseFrom(byteArray);
        DescriptorProtos.FileDescriptorSet descriptorSet2 = parseFrom.getDescriptorSet();
        ByteString message = parseFrom.getMessage();
        String msgName = parseFrom.getMsgName();

        Descriptors.Descriptor pbDescritpor = null;
        for (DescriptorProtos.FileDescriptorProto fdp : descriptorSet2
                .getFileList()) {
            Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor
                    .buildFrom(fdp, new Descriptors.FileDescriptor[] {});
            for (Descriptors.Descriptor descriptor : fileDescriptor
                    .getMessageTypes()) {
                if (descriptor.getName().equals(msgName)) {
                    System.out.println("descriptor found");
                    pbDescritpor = descriptor;
                    break;
                }
            }
        }

        if (pbDescritpor == null) {
            System.out.println("No matched descriptor");
            return;
        }
        DynamicMessage dmsg = DynamicMessage.parseFrom(pbDescritpor, message);

        System.out.println(dmsg);

        Cinema.Ticket ticket = Cinema.Ticket.parseFrom(dmsg.toByteArray());
        System.out.println(ticket);

        String jsonMessage = JsonFormat.printer().print(dmsg);
        System.out.println(jsonMessage);
    }

    public static Cinema.Ticket initMsg() {
        //同上
        Cinema.Movie.Builder movieBuilder = Cinema.Movie.newBuilder();
        movieBuilder.setName("The Shining");
        movieBuilder.setType(Cinema.MovieType.ADULT);
        movieBuilder.setReleaseTimeStamp(327859200);
        Cinema.Movie movie = movieBuilder.build();
        // byte[] byteArray = movie.toByteArray();

        Cinema.Movie.Builder movieBuilder1 = Cinema.Movie.newBuilder();
        movieBuilder1.setName("The Shining1");
        movieBuilder1.setType(Cinema.MovieType.CHILDREN);
        movieBuilder1.setReleaseTimeStamp(327859201);
        Cinema.Movie movie1 = movieBuilder1.build();

        Cinema.Customer.Builder customerBuilder = Cinema.Customer.newBuilder();
        customerBuilder.setName("echo");
        customerBuilder.setGender(Cinema.Gender.MAN);
        customerBuilder.setBirthdayTimeStamp(1231232333);

        Cinema.Ticket.Builder ticketBuilder = Cinema.Ticket.newBuilder();
        ticketBuilder.setId(1);
        ticketBuilder.addMovie(movie);
        ticketBuilder.addMovie(movie1);
        ticketBuilder.setCustomer(customerBuilder.build());
        return ticketBuilder.build();
    }

}
