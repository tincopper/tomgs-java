package com.tomgs.flink.demo.log.model;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tomgs
 * @since 2021/4/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEvent {
  // head
  private String appName;

  private Long timestamp;

  // body
  private JSONObject message;

}
