package com.tomgs.learning.apaas.model;

import lombok.Builder;
import lombok.Data;

/**
 * HttpDynamicObject
 *
 * @author tomgs
 * @since 1.0
 */
@Data
@Builder
public class HttpDynamicObject {

    private String url;

    private HttpMethod method;

    private String contentType;

    private String body;

    private DynamicObject dynamicObject;

}
