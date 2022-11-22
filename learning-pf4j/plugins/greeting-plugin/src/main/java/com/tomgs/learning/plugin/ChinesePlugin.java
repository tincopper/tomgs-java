package com.tomgs.learning.plugin;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

/**
 * ChinesePlugin
 *
 * @author tomgs
 * @since 1.0
 */
public class ChinesePlugin extends Plugin {

    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public ChinesePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Chinese plugin start.");
        super.start();
    }

    @Override
    public void stop() {
        System.out.println("Chinese plugin stop.");
        super.stop();
    }
}
