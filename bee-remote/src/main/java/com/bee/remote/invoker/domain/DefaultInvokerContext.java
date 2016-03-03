package com.bee.remote.invoker.domain;

import com.bee.remote.common.codec.domain.AbstractInvocationContext;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.config.InvokerConfig;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public class DefaultInvokerContext extends AbstractInvocationContext implements InvokerContext{

    private InvokerConfig<?> invokerConfig;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] arguments;
    private Client client;
    private String messageType;

    public DefaultInvokerContext(InvokerConfig<?> invokerConfig, String methodName,
                                 Class<?>[] parameterTypes, Object[] arguments) {
        this.invokerConfig = invokerConfig;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    @Override
    public InvokerConfig<?> getInvokerConfig() {
        return invokerConfig;
    }

    public void setInvokerConfig(InvokerConfig<?> invokerConfig) {
        this.invokerConfig = invokerConfig;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
