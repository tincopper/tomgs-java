package com.tomgs.es.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tangzhongyuan
 * @since 2019-04-24 15:50
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "es")
public class EsProperties {

    //@Value("${es.cluster-name}")
    private String clusterName;

    //@Value("${es.host}")
    private String host;

    //@Value("${es.port}")
    private int tcpPort;

    private int httpPort;

}

