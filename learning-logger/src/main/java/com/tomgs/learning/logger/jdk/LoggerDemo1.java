package com.tomgs.learning.logger.jdk;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * jdk 自带日志工具
 *
 * @author tomgs
 * @since 2021/2/22
 */
public class LoggerDemo1 {

  public static Logger log = Logger.getLogger(LoggerDemo1.class.getName());

  public static void main(String[] args) {
    // all→finest→finer→fine→config→info→warning→server→off
    // 级别依次升高，后面的日志级别会屏蔽之前的级别
    log.setLevel(Level.INFO);
    log.finest("finest");
    log.finer("finer");
    log.fine("fine");
    log.config("config");
    log.info("info");
    log.warning("warning");
    log.severe("server");
  }

}
