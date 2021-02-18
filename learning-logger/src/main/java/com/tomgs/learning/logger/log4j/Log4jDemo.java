package com.tomgs.learning.logger.log4j;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 *  
 *
 * @author tomgs
 * @version 2021/2/16 1.0 
 */
public class Log4jDemo {
    private static final String DEFAULT_LAYOUT = "%d{dd-MM-yyyy HH:mm:ss z} %c{1} %p - %m\n";

    public static void main(String[] args) {
        String loggerName = "tetLog4j";
        Logger logger = getLogger(loggerName);

        logger.info("123123211");
        logger.info("123123211");
    }

    private static Logger getLogger(String loggerName) {
        //Logger logger = Logger.getLogger(loggerName);  // log4j里面的，log4j2没有了，但是里面起实现为下面的
        Logger logger = LogManager.getLogger(loggerName);

        LoggerContext context = LoggerContext.getContext(false);
        Configuration config = context.getConfiguration();

        Layout<String> layout = PatternLayout.newBuilder().withPattern(DEFAULT_LAYOUT).build();
//        PatternLayout layout = PatternLayout.createDefaultLayout(config);

        //final RollingFileAppender fileAppender = RollingFileAppender.newBuilder().build();
        //fileAppender.setMaxBackupIndex(this.jobLogBackupIndex);
        //fileAppender.setMaxFileSize(this.jobLogChunkSize);

        String logPath = config.getProperties().get("log-path");
        if (StringUtils.isBlank(logPath)) {
            logPath = FilenameUtils.concat(System.getProperty("user.home"), "task-logs");
        }
        logPath = "./logs/";
        String fileName = FilenameUtils.concat(logPath, loggerName + ".log");


        // Define file appender with layout and output log file name
        Appender fileAppender = FileAppender.newBuilder().setConfiguration(config) //
                .withAppend(true)
                .withName("programmaticFileAppender") //
                .withLayout(layout) //
                .withFileName(fileName) //
                .build();

        fileAppender.start();

        // cast org.apache.logging.log4j.Logger to org.apache.logging.log4j.core.Logger
        org.apache.logging.log4j.core.Logger loggerImpl = (org.apache.logging.log4j.core.Logger)logger;
        loggerImpl.addAppender(fileAppender);
        loggerImpl.setAdditive(true);
        return logger;
    }

}
