package com.bee.register.zookeeper;

import com.bee.common.constants.Constants;
import org.apache.commons.lang.StringUtils;

/**
 * Created by jeoy.zhou on 12/22/15.
 */
public class CuratorParam {

    private Integer retries;
    private Integer retryInterval;
    private Integer sessionTimeOut;
    private Integer connectTimeOut;
    private String address;
    private String nameSpace;



    public CuratorParam(String address) {
        this(address, null, StringUtils.EMPTY, null, null, null);
    }

    public CuratorParam(String address, String nameSpace) {
        this(address, nameSpace, StringUtils.EMPTY, null, null, null);
    }

    public CuratorParam(String address, Integer retries, Integer retryInterval) {
        this(address, null, retries, retryInterval, null, null);
    }

    public CuratorParam(String address, String nameSpace, Integer retries,
                        Integer retryInterval, Integer sessionTimeOut, Integer connectTimeOut) {
        if(StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("CuratorRegister: zookeeper address is null");
        }
        this.address = address;
        this.nameSpace = StringUtils.isBlank(nameSpace) ? Constants.ZOOKEEPER_DEFAULT_NAMESPACE : nameSpace;
        this.retries = retries == null ? Constants.ZOOKEEPER_DEFAULT_RETRY_TIME : retries;
        this.retryInterval = retryInterval == null ? Constants.ZOOKEEPER_DEFAULT_RETRY_INTERVAL : retryInterval;
        this.sessionTimeOut = sessionTimeOut == null ? Constants.ZOOKEEPER_DEFAULT_SESSION_TIMEOUT : sessionTimeOut;
        this.connectTimeOut = connectTimeOut == null ? Constants.ZOOKEEPER_DEFAULT_CONNECTION_TIMEOUT : connectTimeOut;
    }

    public CuratorParam(String address, String nameSpace, String retries, String retryInterval, String sessionTimeOut, String connectTimeOut) {
        if(StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("CuratorRegister: zookeeper address is null");
        }
        this.address = address;
        this.nameSpace = StringUtils.isBlank(nameSpace) ? Constants.ZOOKEEPER_DEFAULT_NAMESPACE : nameSpace;
        this.retries = StringUtils.isBlank(retries) ? Constants.ZOOKEEPER_DEFAULT_RETRY_TIME : Integer.valueOf(retries);
        this.retryInterval = StringUtils.isBlank(retryInterval) ? Constants.ZOOKEEPER_DEFAULT_RETRY_INTERVAL : Integer.valueOf(retryInterval);
        this.sessionTimeOut = StringUtils.isBlank(retryInterval) ? Constants.ZOOKEEPER_DEFAULT_SESSION_TIMEOUT : Integer.valueOf(sessionTimeOut);
        this.connectTimeOut = StringUtils.isBlank(connectTimeOut) ? Constants.ZOOKEEPER_DEFAULT_CONNECTION_TIMEOUT  : Integer.valueOf(connectTimeOut);
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Integer connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public String getAddress() {
        return address;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
