package com.tomgs.learning.logger.jdk;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 自定义日志处理器
 *
 * @author tomgs
 * @since 2021/2/22
 */
public class RecordHandler extends Handler {

  private final FileHandler fileHandler;

  public RecordHandler(String fileName) throws IOException {
    this.fileHandler = new FileHandler(fileName, 1024 * 1024 * 4, 4, true);
  }

  @Override
  public void publish(LogRecord record) {
    fileHandler.publish(record);
  }

  @Override
  public void flush() {
    fileHandler.flush();
  }

  @Override
  public void close() throws SecurityException {
    fileHandler.close();
  }

}
