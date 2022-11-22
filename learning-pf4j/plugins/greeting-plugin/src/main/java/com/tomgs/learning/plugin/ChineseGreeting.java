package com.tomgs.learning.plugin;

import com.tomgs.learning.api.Greeting;
import org.pf4j.Extension;

/**
 * ChineseGreeting
 *
 * @author tomgs
 * @since 1.0
 */
@Extension
public class ChineseGreeting implements Greeting {

    @Override
    public String message(String name) {
        return "你好," + name;
    }

}
