package com.tomgs.flink.demo.log.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmMatchMode {

    SIMPLE("SIMPLE", "简单匹配"),
    REGEX("REGEX", "正则匹配"),
    CUSTOM("CUSTOM", "自定义匹配");

    private String value;
    private String desc;
}