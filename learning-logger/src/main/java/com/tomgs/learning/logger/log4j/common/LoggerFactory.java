package com.tomgs.learning.logger.log4j.common;

import java.io.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * @author tomgs
 * @since 2021/2/19
 */
public class LoggerFactory {

  private static final String DEFAULT_LAYOUT = "[%d{yyyy-MM-dd HH:mm:ss}] [%X{requestId}] [%thread] %-5level %logger{40} [%L] %msg%n";

  private final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
  private final Configuration config = ctx.getConfiguration();

  public Logger createLogger(String loggerName) {
    Logger logger = LogManager.getLogger(loggerName);

    String logPath = "./logs/customer";
    String logName = "test-" + loggerName + ".log";
    final File logFile = new File(logPath, logName);

    attachFileAppender(logger, logFile);
    return logger;
  }

  public void attachFileAppender(Logger logger, File logFile) {
    String loggerName = logger.getName();
    Layout<String> layout = PatternLayout.newBuilder().withPattern(DEFAULT_LAYOUT).build();
    FileAppender fileAppender = FileAppender.newBuilder()
        .setConfiguration(config)
        .withAppend(true)
        .withName(loggerName)
        .withFileName(logFile.getAbsolutePath())
        .withLayout(layout)
        .build();
    fileAppender.start();

    AppenderRef ref = AppenderRef.createAppenderRef(loggerName, null, null);
    AppenderRef[] refs = new AppenderRef[] { ref };

    LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.ALL, loggerName, "true", refs, null, config, null);
    loggerConfig.addAppender(fileAppender, null, null);

    config.addAppender(fileAppender);
    config.addLogger(loggerName, loggerConfig);

    ctx.updateLoggers();
  }

  public void stopLogger(String loggerName) {
    config.getAppender(loggerName).stop();
    config.getLoggerConfig(loggerName).removeAppender(loggerName);
    config.removeLogger(loggerName);
    ctx.updateLoggers();
  }

  public static void main(String[] args) {
    LoggerFactory factory = new LoggerFactory();
    for (int i = 0; i < 3; i++) {
      Logger logger = factory.createLogger("" + i);
      //logger.info("aa{}cc", "bb");
    }
  }

}
