package com.tomgs.learning.plugin;

import com.tomgs.learning.api.Greeting;
import org.pf4j.Extension;

/**
 * EnglishGreeting
 *
 * @author tomgs
 * @since 1.0
 */
@Extension
public class EnglishGreeting implements Greeting {

    @Override
    public String message(String name) {
        return "Hi, " + name;
    }

}