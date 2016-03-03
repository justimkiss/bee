package com.bee.remote.invoker.config;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.SerializerFactory;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public class InvokerConfig<T> {

    private Class<T> serviceInterface;
    private String url;
    private String version;
    private String callType = Constants.CALL_SYNC;
    private byte serialize = Constants.SERIALIZER_PROTO;
    private int retries = Constants.CLIENT_RETRIES;
    private int timeOut = Constants.CLIENT_CALL_TIMEOUT;
    private boolean isTimeOutRetry = Constants.CLIENT_TIMEOUT_RETRY;
    private String protocol = Constants.PROTOCOL_DEFAULT;
    private String cluster = Constants.CLUSTER_FAILFAST;


    public InvokerConfig(Class<T> serviceInterface, String url,
                         String callType, String serialize, int retries, int timeOut,
                         boolean isTimeOutRetry, String protocol) {
        setServiceInterface(serviceInterface);
        setUrl(url);
        setCallType(callType);
        setSerialize(SerializerFactory.getSerialize(serialize));
        setRetries(retries);
        setTimeOut(timeOut);
        setIsTimeOutRetry(isTimeOutRetry);
        setProtocol(protocol);
    }

    public Class<T> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<T> serviceInterface) {
        if(serviceInterface == null)
            throw new IllegalArgumentException("InvokerConfig: serviceInterface is null");
        if(!serviceInterface.isInterface())
            throw new IllegalArgumentException("InvokerConfig: serviceInterface must be an interface");
        this.serviceInterface = serviceInterface;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public byte getSerialize() {
        return serialize;
    }

    public void setSerialize(byte serialize) {
        this.serialize = serialize;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public boolean isTimeOutRetry() {
        return isTimeOutRetry;
    }

    public void setIsTimeOutRetry(boolean isTimeOutRetry) {
        this.isTimeOutRetry = isTimeOutRetry;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
