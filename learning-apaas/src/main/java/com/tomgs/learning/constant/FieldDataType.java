package com.tomgs.learning.constant;

/**
 * FieldDataType
 *
 * @author tomgs
 * @since 1.0
 */
public enum FieldDataType {

    INT("int"),

    DOUBLE("double"),

    LONG("long"),

    STRING("string"),

    DATE("date"),

    BIG_DECIMAL("big_decimal"),

    OBJECT("object"),

    DYN_OBJECT("dyn_object");

    private String type;

    FieldDataType(String type) {
        this.type = type;
    }

    public String getName() {
        return type;
    }

}
