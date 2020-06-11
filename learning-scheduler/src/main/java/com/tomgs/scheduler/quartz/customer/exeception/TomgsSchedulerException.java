package com.tomgs.scheduler.quartz.customer.exeception;

/**
 * @author tangzy
 * @since 1.0
 */
public class TomgsSchedulerException extends RuntimeException {

  public TomgsSchedulerException(final String errorMessage, final Object... args) {
    super(String.format(errorMessage, args));
  }

  public TomgsSchedulerException(final Throwable cause) {
    super(cause);
  }

}
