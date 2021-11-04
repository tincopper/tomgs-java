package com.tomgs.learning.proto.base;

import com.google.api.AnnotationsProto;
import com.google.api.HttpRule;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.UnknownFieldSet;
import com.tomgs.api.dubbo.DubboProto;
import com.tomgs.api.dubbo.DubboRule;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * ExtDemoTest
 *
 * @author tomgs
 * @since 2021/11/2
 */
public class ExtDemoTest {

    public static void main(String[] args) throws Exception {
        String descSource = System.getProperty("user.dir") + "/learning-proto/custom-protoc-plugin/src/main/proto/ext_demo.desc";

        ParseProto parseProto = new ParseProto();
        Map<String, Integer> extendInfo = parseProto.getExtendInfo(descSource);
        System.out.println(extendInfo);

        System.out.println("-----------------------------");

        Map<String, Object> msgInfo = parseProto.getMsgInfo(descSource);
        System.out.println(msgInfo);

        ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
        DubboProto.registerAllExtensions(extensionRegistry);
        AnnotationsProto.registerAllExtensions(extensionRegistry);

        DescriptorProtos.registerAllExtensions(extensionRegistry);

        DescriptorProtos.FileDescriptorSet fdSet = DescriptorProtos.FileDescriptorSet.parseFrom(new FileInputStream(descSource), extensionRegistry);
        List<DescriptorProtos.FileDescriptorProto> fileList = fdSet.getFileList();
        for (DescriptorProtos.FileDescriptorProto proto : fileList) {
            DescriptorProtos.FileOptions options = proto.getOptions();
            List<DescriptorProtos.ServiceDescriptorProto> serviceList = proto.getServiceList();
            System.out.println(serviceList);
            for (DescriptorProtos.ServiceDescriptorProto serviceDesc : serviceList) {
                DescriptorProtos.ServiceOptions serviceOptions = serviceDesc.getOptions();
                System.out.println(serviceOptions);
                String name = serviceDesc.getName();
                System.out.println(name);
                List<DescriptorProtos.MethodDescriptorProto> methodList = serviceDesc.getMethodList();
                System.out.println(methodList);
                for (DescriptorProtos.MethodDescriptorProto methodDesc : methodList) {
                    String methodDescName = methodDesc.getName();
                    System.out.println(methodDescName);
                    DescriptorProtos.MethodOptions methodDescOptions = methodDesc.getOptions();
                    System.out.println(methodDescOptions);

                    HttpRule httpRule = methodDescOptions.getExtension(AnnotationsProto.http);
                    System.out.println(httpRule);

                    DubboRule dubboRule = methodDescOptions.getExtension(DubboProto.dubbo);
                    System.out.println(dubboRule);

                    UnknownFieldSet uf = methodDescOptions.getUnknownFields();
                    for (Map.Entry<Integer, UnknownFieldSet.Field> entry : uf.asMap().entrySet()) {
                        Integer entryKey = entry.getKey();
                        UnknownFieldSet.Field val = entry.getValue();
                        //DubboRule dubboRule = DubboRule.parseFrom(val.getLengthDelimitedList().get(0));
                        //System.out.println(dubboRule);
                    }

                    System.out.println(methodDesc.getInputType());
                    System.out.println(methodDesc.getOutputType());
                }
            }
        }

    }

}
