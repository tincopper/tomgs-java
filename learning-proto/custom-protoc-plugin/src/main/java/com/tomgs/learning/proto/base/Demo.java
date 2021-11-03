package com.tomgs.learning.proto.base;

import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo
 * 不能直接用protobuf提供的库来解析.proto文件，
 * 但是可以用它提供的解析的.desc文件来获取.proto文件信息。所以要先调用如下命令生成desc文件：
 *
 * @author tomgs
 * @since 2021/11/1
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        Runtime run = Runtime.getRuntime();

        String dir = System.getProperty("user.dir");
        String source = dir + "/learning-proto/custom-protoc-plugin/src/main/proto/";
        String cmd = "cmd /c " + "protoc.exe -I=" + source + " --descriptor_set_out=" + source + "helloworld.desc " + source + "helloworld.proto";
        System.out.println(cmd);

        Process p = run.exec(cmd);

        // 如果不正常终止, 则生成desc文件失败
        if (p.waitFor() != 0) {
            if (p.exitValue() == 1) {//p.exitValue()==0表示正常结束，1：非正常结束
                System.err.println("命令执行失败!");
                System.exit(1);
            }
        }

        Map<String, String> mapping = new HashMap<String, String>();

        FileInputStream fin = new FileInputStream(source + "/helloworld.desc");
        FileDescriptorSet descriptorSet = FileDescriptorSet.parseFrom(fin);

        for (FileDescriptorProto fdp : descriptorSet.getFileList()) {

            FileDescriptor fd = FileDescriptor.buildFrom(fdp, new FileDescriptor[]{});
            for (Descriptor descriptor : fd.getMessageTypes()) {
                String className = fdp.getOptions().getJavaPackage() + "."
                        + fdp.getOptions().getJavaOuterClassname() + "$"
                        + descriptor.getName();
                List<FieldDescriptor> types = descriptor.getFields();
                for (FieldDescriptor type : types) {
                    System.out.println(type.getFullName());
                }
                System.out.println(descriptor.getFullName() + " -> " + className);
            }
        }


//		Descriptor md = fd.getDescriptorForType();
//		byte[] data = null ;
//		DynamicMessage m = DynamicMessage.parseFrom(md, data);

    }

}