package com.bee.remote.invoker.domain;

import com.bee.common.constants.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class ConnectInfo {

    private String host;
    private int port;
    private ConcurrentMap<String, Integer> serviceNames = Maps.newConcurrentMap();

    public ConnectInfo(String host, Integer port, Integer weight) {
        this.host = host;
        this.port = port;
        serviceNames.putIfAbsent(host, weight);
    }

    public void addServiceNames(Map<String, Integer> serviceNames) {
        if(MapUtils.isNotEmpty(serviceNames)) {
            serviceNames.putAll(serviceNames);
        }
    }

    public String getConnect() {
        return host + Constants.COLON_SYMBOL + port;
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

    public ConcurrentMap<String, Integer> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(ConcurrentHashMap<String, Integer> serviceNames) {
        this.serviceNames = serviceNames;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
