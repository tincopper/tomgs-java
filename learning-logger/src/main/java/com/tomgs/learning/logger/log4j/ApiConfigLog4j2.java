package com.tomgs.learning.logger.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.Map;

/**
 * api方式配置log4j2
 *
 * @author tomgs
 * @version 1.0
 */
public class ApiConfigLog4j2 {

    public static void main(String[] args) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        //Configuration configuration = context.getConfiguration();
        //Configurator.initialize(configuration);

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();
        /*FileAppender appender = FileAppender.newBuilder()
                .withFileName("/path/to/logfile.log")
                .setLayout(layout)
                .setName("FileAppender")
                .build();
        appender.start();*/
        ConsoleAppender appender = ConsoleAppender.newBuilder()
                .setLayout(layout)
                .setName("Console")
                .build();
        appender.start();

        Map<String, Appender> appenders = context.getRootLogger().getAppenders();
        System.out.println(appenders);

        //context.getConfiguration().addAppender(appender);
        context.getRootLogger().addAppender(appender);
        context.updateLoggers();

        Logger logger = context.getLogger("test");
        logger.setLevel(Level.ALL);
        logger.info("test123");

        logger.getContext().stop();
        //context.stop();
    }

}
