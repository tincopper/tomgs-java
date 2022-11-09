package com.tomgs.learning.apaas.constant;

/**
 * DataBaseType
 *
 * @author tomgs
 * @since 1.0
 */
public enum DataBaseType {
    MYSQL("mysql"),

    PGSQL("postgresql"),

    REDIS("redis");

    private final String type;

    DataBaseType(String type) {
        this.type = type;
    }

    public String getName() {
        return type;
    }

}
