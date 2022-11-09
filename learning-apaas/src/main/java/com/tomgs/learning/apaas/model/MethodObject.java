package com.tomgs.learning.apaas.model;

import com.tomgs.learning.apaas.constant.DataStructType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * MethodObject
 *
 * @author tomgs
 * @since 1.0
 */
@Data
@Builder
public class MethodObject {

    private Long id;

    private String name;

    private List<ParameterObject> params;

    private DataStructType resultStructType;

    private Long resultDynObjId;

}
