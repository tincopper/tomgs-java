package com.tomgs.learning.apaas.model;

import cn.hutool.core.util.StrUtil;
import com.tomgs.learning.apaas.constant.FieldOpDef;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Argument
 *
 * @author tomgs
 * @since 1.0
 */
@Data
@Builder
public class Argument {

    private String name;

    private FieldOpDef op;

    private List<Object> value;

    public String toSqlString() {
        switch (op) {
            case IN:
            case NOT_IN:
                return name + " " + op.getOpValue() + " (" + StrUtil.join(",", value) + ")";
            case BETWEEN:
                return name + " " + op.getOpValue() + " " + value.get(0) + " AND " + value.get(1);
            default:
                return name + " " + op.getOpValue() + " " + value.get(0);
        }
    }

}
