package com.tomgs.proto.test;

import com.google.protobuf.compiler.PluginProtos;
import com.salesforce.jprotoc.ProtocPluginTesting;
import com.tomgs.proto.gen.demo.DubboExtensionGenerator;
import org.junit.Test;

import java.io.IOException;

/**
 * DubboGeneratorTest
 *
 * @author tomgs
 * @since 2022/9/6
 */
public class DubboGeneratorTest {

    @Test
    public void verifyGeneratorWorks() throws IOException {
        PluginProtos.CodeGeneratorResponse response = ProtocPluginTesting.test(new DubboExtensionGenerator(), "target/generated-test-sources/protobuf/java/tomgs_dump");
        System.out.println(response.getError());
        System.out.println(response.getFileList());
    }

}
