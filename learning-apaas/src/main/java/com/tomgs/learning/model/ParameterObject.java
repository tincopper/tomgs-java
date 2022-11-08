package com.tomgs.learning.model;

import com.tomgs.learning.constant.DataStructType;
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
