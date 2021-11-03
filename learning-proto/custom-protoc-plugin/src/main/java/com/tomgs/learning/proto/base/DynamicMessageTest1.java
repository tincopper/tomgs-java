package com.tomgs.learning.proto.base;

import com.google.protobuf.*;
import com.tomgs.learning.cinema.Cinema;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * DynamicMessage1Test
 *
 *  //生成java源文件
 * D:\>protoc.exe --java_out=./ ./cinema1.proto
 *
 * //生成descriptor，这里通过命令行延时如何生成的，下面的java代码里每次会通过命令生成
 * D:\>protoc.exe --descriptor_set_out=D://cinema1.description D://cinema1.proto --proto_path=D://
 *
 * @author tomgs
 * @since 2021/11/2
 */
public class DynamicMessageTest1 {

    public static void main(String[] args) throws Exception {
        while (true) {
            test();
            break;
        }
    }

    private static void test() throws IOException, InterruptedException,
            FileNotFoundException, Descriptors.DescriptorValidationException,
            InvalidProtocolBufferException {
        String source = System.getProperty("user.dir") + "/learning-proto/custom-protoc-plugin/src/main/proto/";
        System.out.println("------init msg------");
        byte[] byteArray = initMsg();

        /*System.out.println("------generate descriptor------");
        // 生成descriptor文件
        String protocCMD = "protoc --descriptor_set_out=D://cinema1.description D://cinema1.proto --proto_path=D://";
        Process process = Runtime.getRuntime().exec(protocCMD);
        process.waitFor();
        int exitValue = process.exitValue();
        if (exitValue != 0) {
            System.out.println("protoc execute failed");
            return;
        }*/

        System.out.println("------customer msg------");
        Descriptors.Descriptor pbDescritpor = null;
        DescriptorProtos.FileDescriptorSet descriptorSet = DescriptorProtos.FileDescriptorSet
                .parseFrom(new FileInputStream(source + "/cinema.desc"));
        for (DescriptorProtos.FileDescriptorProto fdp : descriptorSet.getFileList()) {
            Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(fdp, new Descriptors.FileDescriptor[] {});
            for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
                String className = fdp.getOptions().getJavaPackage() + "."
                        + fdp.getOptions().getJavaOuterClassname() + "$"
                        + descriptor.getName();
                System.out.println(descriptor.getFullName() + " -> "+ className);

                if (descriptor.getName().equals("Ticket")) {
                    System.out.println("Movie descriptor found");
                    pbDescritpor = descriptor;
                    break;
                }
            }
        }

        if (pbDescritpor == null) {
            System.out.println("No matched descriptor");
            return;
        }
        DynamicMessage.Builder pbBuilder = DynamicMessage.newBuilder(pbDescritpor);
        Message pbMessage = pbBuilder.mergeFrom(byteArray).build();

        //DynamicMessage parseFrom = DynamicMessage.parseFrom(pbDescritpor, byteArray);

        System.out.println(pbMessage);
    }

    public static byte[] initMsg() {
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
        Cinema.Ticket ticket = ticketBuilder.build();
        System.out.println(ticket.toString());
        byte[] byteArray = ticket.toByteArray();

        return byteArray;
        //return null;
    }
}
