package com.tomgs.gen.test;

import com.google.protobuf.compiler.PluginProtos;
import com.salesforce.jprotoc.Generator;
import com.salesforce.jprotoc.GeneratorException;
import com.salesforce.jprotoc.ProtoTypeMap;
import com.salesforce.jprotoc.ProtocPlugin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * TestGenerator
 *
 * @author tomgs
 * @since 2022/9/5
 */
public class TestGenerator extends Generator {

    public static void main(String[] args) {
        if (args.length == 0) {
            // Generate from protoc via stdin
            ProtocPlugin.generate(new TestGenerator());
        } else {
            // Process from a descriptor_dump file via command line arg
            ProtocPlugin.debug(new TestGenerator(), args[0]);
        }
    }

    @Override
    protected List<PluginProtos.CodeGeneratorResponse.Feature> supportedFeatures() {
        return Collections.singletonList(PluginProtos.CodeGeneratorResponse.Feature.FEATURE_PROTO3_OPTIONAL);
    }

    @Override
    public Stream<PluginProtos.CodeGeneratorResponse.File> generate(PluginProtos.CodeGeneratorRequest request)
            throws GeneratorException {

        // create a map from proto types to java types
        final ProtoTypeMap protoTypeMap = ProtoTypeMap.of(request.getProtoFileList());

        // set context attributes by extracting values from the request
        // use protoTypeMap to translate between proto types and java types
        Context ctx = new Context();

        // generate code from an embedded resource Mustache template
        String content = applyTemplate("myTemplate.mustache", ctx);

        // create a new file for protoc to write
        PluginProtos.CodeGeneratorResponse.File file = PluginProtos.CodeGeneratorResponse.File
                .newBuilder()
                .setName("fileName")
                .setContent(content)
                .build();

        return Collections.singletonList(file).stream();
    }

    private class Context {
        // attributes for use in your code template
    }

}
