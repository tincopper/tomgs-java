package com.tomgs.learning;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws ClassNotFoundException {
        final Class<?> superClass = Class.forName("com.tomgs.learning.api.Greeting");
        final Class<?> subClass = Class.forName("com.tomgs.learning.plugin.ChineseGreeting");
        System.out.println(superClass.isAssignableFrom(subClass));
    }
}
