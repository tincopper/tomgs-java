package com.tomgs.guice.test;

import com.google.inject.*;

import javax.inject.Named;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 11:22
 **/
public class PlayerClient {

    private Player badPlayer;

    @Inject
    public void PlayerClient(@Named("bad") Player badPlayer) {
        this.badPlayer = badPlayer;
    }

    public static void main(String[] args) {
        PlayerModule module = new PlayerModule();
        Injector injector = Guice.createInjector(module);

        Map<Key<?>, Binding<?>> allBindings = injector.getAllBindings();
        System.out.println(allBindings.toString());

        Player goodPlayer = injector.getInstance(Key.get(Player.class, Good.class));
        goodPlayer.bat();
        goodPlayer.bowl();

        Player badPlayer = injector.getInstance(Key.get(Player.class, Bad.class));
        badPlayer.bat();
        badPlayer.bowl();

        PlayerClient client = injector.getInstance(PlayerClient.class);
        client.badPlayer.bat();
        client.badPlayer.bowl();

    }
}
