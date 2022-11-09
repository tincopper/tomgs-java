package com.tomgs.learning.apaas.model;

import com.tomgs.learning.apaas.constant.DataStructType;
import com.tomgs.learning.apaas.constant.FieldDataType;
import lombok.Builder;
import lombok.Data;

/**
 * FieldObject
 *
 * @author tomgs
 * @since 1.0
 */
@Data
@Builder
public class FieldObject {

    private Long id;

    private String name;

    private String jsonName;

    private Boolean isPrimaryKey;

    private FieldDataType fieldDataType;

    private DataStructType fieldDataStructType;

    private Object initValue;

    private Long dynObjectId;

}
