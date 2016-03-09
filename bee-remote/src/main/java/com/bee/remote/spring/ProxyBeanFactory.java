package com.bee.remote.spring;

import com.bee.common.constants.Constants;
import com.bee.common.util.ClassUtils;
import com.bee.remote.invoker.config.InvokerConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public class ProxyBeanFactory implements FactoryBean<Object> {

    private String serviceName;
    private String interfaceName;
    private String serialize = Constants.PROTO;
    private String callMethod = Constants.CALL_SYNC;
    private Integer timeOut= Constants.CLIENT_CALL_TIMEOUT;
    private Integer retries = Constants.CLIENT_RETRIES;
    private boolean isTimeOutRetry = Constants.CLIENT_TIMEOUT_RETRY;
    private String protocol = Constants.PROTOCOL_DEFAULT;

    private Object obj;

    private Class<?> objType;

    private void init() throws ClassNotFoundException {
        if(StringUtils.isBlank(interfaceName))
            throw new IllegalArgumentException("ProxyBeanFactory: init argument[interfaceName] is null");
        this.objType = ClassUtils.loadClass(interfaceName);
        InvokerConfig<?> invokerConfig = new InvokerConfig(this.objType, this.serviceName, this.callMethod,
                this.serialize, this.retries, this.timeOut, this.isTimeOutRetry, this.protocol);
        this.obj = ServiceFactory.getService(invokerConfig);
    }

    @Override
    public Object getObject() throws Exception {
        return this.obj;
    }

    @Override
    public Class<?> getObjectType() {
        return this.objType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getSerialize() {
        return serialize;
    }

    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(String callMethod) {
        this.callMethod = callMethod;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
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
}
