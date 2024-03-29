package com.tomgs.learning.apaas.constant;

/**
 * DataStructType
 *
 * @author tomgs
 * @since 1.0
 */
public enum DataStructType {

    LIST("list"),

    SET("set"),

    MAP("map"),

    NONE("none");

    private final String type;

    DataStructType(String type) {
        this.type = type;
    }

    public String getName() {
        return type;
    }

}
