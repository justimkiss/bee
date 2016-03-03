package com.bee.common.entity;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public class UrlConfig {

    private String serviceName;
    private String version;

    public UrlConfig(){}

    public UrlConfig(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
