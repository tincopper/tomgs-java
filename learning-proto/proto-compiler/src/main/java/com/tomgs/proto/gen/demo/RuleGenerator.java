package com.tomgs.proto.gen.demo;

import com.google.common.collect.Lists;
import com.salesforce.jprotoc.ProtocPlugin;
import com.tomgs.api.dubbo.DubboProto;

/**
 * RuleGenerator
 *
 * @author tomgs
 * @since 2021/11/8
 */
public class RuleGenerator extends AbstractExtensionGenerator {

    public static void main(String[] args) {
        if (args.length == 0) {
            ProtocPlugin.generate(Lists.newArrayList(new RuleGenerator()), Lists.newArrayList(DubboProto.dubbo));
        } else {
            ProtocPlugin.debug(Lists.newArrayList(new RuleGenerator()), Lists.newArrayList(DubboProto.dubbo), args[0]);
        }
    }


    @Override
    protected String getClassPrefix() {
        return "";
    }

    @Override
    protected String getClassSuffix() {
        return "CamelRule";
    }

}
