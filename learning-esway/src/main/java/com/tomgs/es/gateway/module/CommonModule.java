package com.tomgs.es.gateway.module;

import com.google.inject.AbstractModule;
import com.tomgs.es.gateway.common.Props;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 15:02
 **/
public class CommonModule extends AbstractModule {

    private final Props props;

    public CommonModule(Props props) {
        this.props = props;
    }

    @Override
    protected void configure() {
        bind(Props.class).toInstance(this.props);
    }
}
