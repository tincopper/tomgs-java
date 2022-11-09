package com.tomgs.learning.apaas.model;

import com.tomgs.learning.apaas.constant.DataStructType;
import lombok.Builder;
import lombok.Data;

/**
 * ParameterObject
 *
 * @author tomgs
 * @since 1.0
 */
@Data
@Builder
public class ParameterObject {

    private Long id;

    private FieldObject fieldObject;

    private DataStructType parameterStructType;

}
