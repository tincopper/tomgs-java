package com.tomgs.learning.logger.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.LevelRangeFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.Charset;

import static org.apache.logging.log4j.core.Filter.Result.DENY;

/**
 *  
 *
 * @author tomgs
 * @version 2021/2/14 1.0 
 */
public class Log4j2Demo {

    public static void main(String[] args) throws InterruptedException {
        String loggerName = "testLog";
        Appender appender = null;

        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration loggerConfiguration = ctx.getConfiguration();

        PatternLayout.Builder layoutBuilder = PatternLayout.newBuilder();
        layoutBuilder.withCharset(Charset.defaultCharset());
        layoutBuilder.withConfiguration(loggerConfiguration);
        layoutBuilder.withPattern("%d{yyyy-MM-dd HH:mm:ss,SSS} %t %-5p %C %c{1}:%L -%m%n");
        Layout<String> layout = layoutBuilder.build();

        //创建一个展示的样式：PatternLayout，   还有其他的日志打印样式。

        //appender = ConsoleAppender.createDefaultAppenderForLayout(layout);

        RollingFileAppender.Builder<?> rollingFileAppenderBuilder = RollingFileAppender.newBuilder();

        rollingFileAppenderBuilder.withImmediateFlush(true);
        rollingFileAppenderBuilder.withBufferedIo(false);
        rollingFileAppenderBuilder.withBufferSize(0);
        //System.getProperties().remove(AsyncPropKey);

        rollingFileAppenderBuilder.withAppend(true);
        rollingFileAppenderBuilder.withFileName("./logs/" + loggerName + "/test.log");
        // 设置日志样式
        //loggerBuilder.withFilePattern(spellBackupFileName(loggerName));
        rollingFileAppenderBuilder.withFilePattern("./logs/" + loggerName + "/test.log.%d{yyyy-MM-dd}");
        rollingFileAppenderBuilder.withLayout(layout);
        rollingFileAppenderBuilder.withName(loggerName);
        rollingFileAppenderBuilder.withPolicy(CompositeTriggeringPolicy.createPolicy(SizeBasedTriggeringPolicy.createPolicy("10MB")));

        //ThresholdFilter filter = ThresholdFilter.createFilter(Level.DEBUG, Filter.Result.ACCEPT, DENY);
        LevelRangeFilter filter = LevelRangeFilter.createFilter(Level.INFO, Level.INFO, Filter.Result.ACCEPT, DENY);
        rollingFileAppenderBuilder.withFilter(filter);
        //DefaultRolloverStrategy.newBuilder() 使用builder方式替换
        rollingFileAppenderBuilder.withStrategy(DefaultRolloverStrategy.newBuilder().withMax("10").build());

        appender = rollingFileAppenderBuilder.build();

        appender.start();
        loggerConfiguration.addAppender(appender);

        AppenderRef appenderRef = AppenderRef.createAppenderRef(loggerName, null, null);
        AppenderRef[] appenderRefs = new AppenderRef[] { appenderRef };

        LoggerConfig loggerConfig = LoggerConfig.createLogger(true, null, loggerName + 123,
                "true", appenderRefs, null, loggerConfiguration, null);

        loggerConfig.addAppender(appender, Level.ALL, null);
        //loggerConfiguration.addLogger("org.apache.logging.log4j", loggerConfig);
        loggerConfiguration.addLogger(loggerName, loggerConfig);
        ctx.updateLoggers();
        //loggerConfiguration.start();
        //ctx.updateLoggers(loggerConfiguration);

        Logger logger = LogManager.getLogger(loggerName);

        logger.info("13123132");
        //logger.info("abc{}abc", 123);
        logger.info("1312313211111111111111");
        //appender.stop();
        logger.info("1312313211111111111111");
        logger.info("1312313211111111111111");


        Thread.sleep(3000);
    }
    
}
