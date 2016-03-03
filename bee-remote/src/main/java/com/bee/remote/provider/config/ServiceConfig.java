package com.bee.remote.provider.config;

import com.bee.common.constants.Constants;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public class ServiceConfig {

    private String ip;
    private int port = Constants.DEFAULT_PORT;
    private int httpPort = Constants.DEFAULT_HTTP_PORT;
    private String protocol = Constants.PROTOCOL_DEFAULT;
    private int corePoolSize = Constants.DEFAULT_PROVIDER_COREPOOLSIZE;
    private int maxPoolSize = Constants.DEFAULT_PROVIDER_MAXPOOLSIZE;
    private int workQueueSize = Constants.DEFAULT_PROVIDER_WORKQUEUESIZE;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }

    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
