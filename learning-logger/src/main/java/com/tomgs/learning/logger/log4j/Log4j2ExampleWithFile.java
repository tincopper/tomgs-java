package com.tomgs.learning.logger.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
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
public class Log4j2ExampleWithFile {

    public static final String DEFAULT_LAYOUT = "%d{yyyy-MM-dd HH:mm:ss} %p %class{40}:%L [%t] %msg%n";

    public static void main(String[] args) {
        // Create configuration builder
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        // Create layout builder
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", DEFAULT_LAYOUT);
        // Create console appender builder
        // RandomAccessFile
        // File
        final AppenderComponentBuilder appenderComponentBuilder = builder.newAppender("file", "RandomAccessFile")
                .addAttribute("fileName", "./test.log")
                .add(layoutBuilder);
        // Add console appender to root logger
        RootLoggerComponentBuilder rootLoggerBuilder = builder.newAsyncRootLogger(Level.INFO, true)
                .add(builder.newAppenderRef("file"));
        // Add appenders and loggers to configuration
        builder.add(appenderComponentBuilder);
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
