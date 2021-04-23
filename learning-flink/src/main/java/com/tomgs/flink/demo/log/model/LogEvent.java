package com.tomgs.flink.demo.log.model;

import java.util.HashMap;

/**
 * @author tomgs
 * @since 2021/4/22
 */
public class LogEvent extends HashMap<String, Object> {

  private String appName;

  public String getAppName() {
    if (this.appName == null) {
      this.appName = String.valueOf(this.get("appname"));
    }
    return this.appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
    this.put("appname", appName);
  }

  @Override
  public String toString() {
    return "LogEvent{" +
        "appName='" + appName + '\'' +
        "other=" + super.toString() +
        '}';
  }

}
