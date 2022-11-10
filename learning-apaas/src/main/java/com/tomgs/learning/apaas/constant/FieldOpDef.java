package com.tomgs.learning.apaas.constant;

public enum FieldOpDef {
    GT(">"),

    GTE(">="),

    LT("<"),

    LTE("<="),

    EQ("="),

    NOT_EQ("!="),

    IN("IN"),

    NOT_IN("NOT IN"),

    LIKE("LIKE"),

    BETWEEN("BETWEEN");

    //OR("OR"),

    //AND("AND"),

    //ORDER_BY("ORDER BY");

    private final String value;

    FieldOpDef(String value) {
        this.value = value;
    }

    public String getOpValue() {
        return this.value;
    }

}
