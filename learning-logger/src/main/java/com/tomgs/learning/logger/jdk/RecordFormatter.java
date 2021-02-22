package com.tomgs.learning.logger.jdk;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * @author tomgs
 * @since 2021/2/22
 */
public class RecordFormatter extends Formatter {

  private final SimpleFormatter formatter = new SimpleFormatter();

  @Override
  public String format(LogRecord record) {
    return formatter.format(record);
  }

}
