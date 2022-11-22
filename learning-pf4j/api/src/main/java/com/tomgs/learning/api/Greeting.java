package com.tomgs.learning.api;

import org.pf4j.ExtensionPoint;

/**
 * Greeting
 *
 * @author tomgs
 * @since 1.0
 */
public interface Greeting extends ExtensionPoint {

    String message(String name);

}
