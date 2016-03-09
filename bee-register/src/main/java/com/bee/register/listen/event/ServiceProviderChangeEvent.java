package com.bee.register.listen.event;

import com.bee.common.constants.Constants;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class ServiceProviderChangeEvent implements Serializable{

    private static final long serialVersionUID = 1772890957479629245L;
    private String host;
    private int port;
    private String serviceName;
    private ProviderChangeEnum providerChangeEnum;


    private int weight = Constants.DEFAULT_WEIGHT;

    public ServiceProviderChangeEvent() {}

    public ServiceProviderChangeEvent(String host, int port, String serviceName, ProviderChangeEnum providerChangeEnum) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
        this.providerChangeEnum = providerChangeEnum;
    }

    public ServiceProviderChangeEvent(String host, int port, String serviceName, int weight, ProviderChangeEnum providerChangeEnum) {
        this(host, port, serviceName, providerChangeEnum);
        this.weight = weight;
    }

    public ProviderChangeEnum getProviderChangeEnum() {
        return providerChangeEnum;
    }

    public void setProviderChangeEnum(ProviderChangeEnum providerChangeEnum) {
        this.providerChangeEnum = providerChangeEnum;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
