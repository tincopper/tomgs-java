package com.tomgs.es.gateway.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.common.inject.internal.ToStringBuilder;

@Data
@Builder
@AllArgsConstructor
public class ServerInfo {

    private String host;
    private int httpPort;
    private int tcpPort;
    private int weight = 100;
    private int retry;
    private long timeout;

    public ServerInfo() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        if (httpPort != that.httpPort) return false;
        if (tcpPort != that.tcpPort) return false;
        return host != null ? host.equals(that.host) : that.host == null;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + httpPort;
        result = 31 * result + tcpPort;
        result = 31 * result + retry;
        result = 31 * result + (int) (timeout ^ (timeout >>> 32));
        return result;
    }

}
