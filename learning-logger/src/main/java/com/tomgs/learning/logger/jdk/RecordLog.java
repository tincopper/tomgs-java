package com.tomgs.learning.logger.jdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tomgs
 * @since 2021/2/22
 */
public class RecordLog {

  private static final Logger recordLog = Logger.getLogger("RecordLog");
  private static final String FILE_NAME = "record.log";
  private static Handler logHandler = makerHandler();

  private static Handler makerHandler() {
    try {
      Handler handler = new RecordHandler("./logs/" + FILE_NAME);
      handler.setEncoding("utf-8");
      handler.setFormatter(new RecordFormatter());
      return handler;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void info(String msg, Object... args) {
    recordLog.setUseParentHandlers(false);
    // Remove all current handlers.
    for (Handler h : recordLog.getHandlers()) {
      recordLog.removeHandler(h);
    }
    // Attach the given handler.
    recordLog.addHandler(logHandler);

    // record
    recordLog.log(Level.INFO, msg, args);
  }


}
