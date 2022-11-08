package com.tomgs.learning.model;

import com.tomgs.learning.constant.DataBaseType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DynamicObject
 *
 * @author tomgs
 * @since 1.0
 */
@Data
@Builder
public class DynamicObject {

    private Long id;

    private String name;

    private DataBaseType dataBaseType;

    private String primaryKey;

    private List<FieldObject> fields;

    private List<MethodObject> methods;

}
