package com.tomgs.guice.test;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 11:12
 **/
public class PlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind(Player.class).to(BadPlayer.class);

        bind(Player.class).annotatedWith(Good.class).to(GoodPlayer.class);
        bind(Player.class).annotatedWith(Bad.class).to(BadPlayer.class);

        bind(Player.class).annotatedWith(Names.named("good")).to(GoodPlayer.class);
        bind(Player.class).annotatedWith(Names.named("bad")).to(BadPlayer.class);

    }
}
