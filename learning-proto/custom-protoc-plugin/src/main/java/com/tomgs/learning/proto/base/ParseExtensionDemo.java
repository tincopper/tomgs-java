package com.tomgs.learning.proto.base;

import com.google.api.AnnotationsProto;
import com.google.api.HttpProto;
import com.google.api.HttpRule;
import com.google.common.io.ByteStreams;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.compiler.PluginProtos;
import com.tomgs.api.dubbo.DubboProto;
import com.tomgs.api.dubbo.DubboRule;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * ParseExtensionDemo
 *
 * @author tomgs
 * @since 2021/11/2
 */
public class ParseExtensionDemo {

    public static void main(String[] args) throws IOException {
        String descSource = System.getProperty("user.dir") + "/learning-proto/custom-protoc-plugin/src/main/proto/ext_demo.desc";

        byte[] generatorRequestBytes = ByteStreams.toByteArray(new FileInputStream(descSource));

        ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
        DubboProto.registerAllExtensions(extensionRegistry);
        AnnotationsProto.registerAllExtensions(extensionRegistry);

        DescriptorProtos.registerAllExtensions(extensionRegistry);

        PluginProtos.CodeGeneratorRequest request = PluginProtos.CodeGeneratorRequest.parseFrom(generatorRequestBytes, extensionRegistry);
        for (String generate : request.getFileToGenerateList()) {
            //DescriptorProtos.FileDescriptorSet.parseFrom();
        }

        List<DescriptorProtos.FileDescriptorProto> fileProtos = request.getProtoFileList();

        for (DescriptorProtos.FileDescriptorProto fileProto : fileProtos) {
            List<DescriptorProtos.ServiceDescriptorProto> serviceList = fileProto.getServiceList();
            for (DescriptorProtos.ServiceDescriptorProto serviceDescriptorProto : serviceList) {
                List<DescriptorProtos.MethodDescriptorProto> methodList = serviceDescriptorProto.getMethodList();
                for (DescriptorProtos.MethodDescriptorProto methodDescriptorProto : methodList) {
                    DescriptorProtos.MethodOptions options = methodDescriptorProto.getOptions();
                    DubboRule dubboRule = options.getExtension(DubboProto.dubbo);
                    System.out.println(dubboRule);

                    HttpRule httpRule = options.getExtension(AnnotationsProto.http);
                    System.out.println(httpRule);
                }
            }
        }

    }

}
