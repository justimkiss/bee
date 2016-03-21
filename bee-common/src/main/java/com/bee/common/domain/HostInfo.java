package com.bee.common.domain;

import com.bee.common.constants.Constants;

import java.io.Serializable;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class HostInfo implements Serializable{

    private static final long serialVersionUID = -4497721447447536662L;
    private String serviceName;
    private String host;
    private int port;
    private int weight;
    private String version;

    public HostInfo() {}

    public HostInfo(String serviceName, String host, int port) {
        this(serviceName, host, port, Constants.DEFAULT_WEIGHT, null);
    }

    public HostInfo(String serviceName, String host, int port, int weight) {
        this(serviceName, host, port, weight, null);
    }

    public HostInfo(String serviceName, String host, int port, int weight, String version) {
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.weight = weight;
        this.version = version;
    }

    public String getConnect() {
        return this.getHost() + Constants.COLON_SYMBOL + this.getPort();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return host.hashCode() + port;
    }

    @Override
    public String toString() {
        return "HostInfo [host=" + host + ", port=" + port + ", weight=" + weight + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HostInfo) {
            HostInfo hp = (HostInfo) obj;
            return this.host.equals(hp.host) && this.port == hp.port;
        }
        return false;
    }
}
