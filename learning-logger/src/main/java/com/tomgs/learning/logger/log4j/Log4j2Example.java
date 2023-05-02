package com.tomgs.learning.logger.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author tomgs
 * @version 1.0
 */
public class Log4j2Example {

    public static void main(String[] args) {
        // Create configuration builder
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        // Create layout builder
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} %p %C{1.} [%t] %m%n");
        // Create console appender builder
        AppenderComponentBuilder consoleAppenderBuilder = builder.newAppender("console", "Console")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(layoutBuilder);
        // Add console appender to root logger
        RootLoggerComponentBuilder rootLoggerBuilder = builder.newRootLogger(Level.INFO)
                .add(builder.newAppenderRef("console"));
        // Add appenders and loggers to configuration
        builder.add(consoleAppenderBuilder);
        builder.add(rootLoggerBuilder);
        // Build configuration and start logger context
        Configuration config = builder.build();
        LoggerContext context = Configurator.initialize(config);
        // Get logger and log messages
        //Logger logger = context.getLogger(Log4j2Example.class.getName());
        Logger logger = context.getRootLogger();
        logger.info("Log4j2 configured using Java API");
        logger.debug("Debug message");
        // Sleep for a few seconds to allow time for messages to be logged
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Shutdown logger context
        Configurator.shutdown(context);
    }

}
