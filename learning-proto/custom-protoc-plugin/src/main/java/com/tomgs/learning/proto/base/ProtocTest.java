package com.tomgs.learning.proto.base;

import org.junit.Test;

/**
 * ProtocTest
 *
 * @author tomgs
 * @since 2021/11/2
 */
public class ProtocTest {

    private final ParseProto parseProto = new ParseProto();

    @Test
    public void testGenerateJavaSource() throws Exception {
        String protoName = "cinema.proto";
        parseProto.genJavaSource(protoName);
    }

    @Test
    public void testGenerateDescProto() throws Exception {
        String protoName = "cinema.proto";
        parseProto.genProtoDesc(protoName);
    }

    @Test
    public void testGenerateExtDemoDescProto() throws Exception {
        String protoName = "ext_demo.proto";
        parseProto.genProtoDesc(protoName);
    }

}
