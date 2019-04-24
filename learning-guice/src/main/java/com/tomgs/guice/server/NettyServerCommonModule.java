package com.tomgs.guice.server;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.tomgs.guice.server.common.Props;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 15:02
 **/
public class NettyServerCommonModule extends AbstractModule {

    private final Props props;

    public NettyServerCommonModule(Props props) {
        this.props = props;
    }

    @Override
    protected void configure() {
        bind(Props.class).toInstance(this.props);
    }
}
