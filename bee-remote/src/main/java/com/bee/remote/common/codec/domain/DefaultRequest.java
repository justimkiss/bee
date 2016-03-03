package com.bee.remote.common.codec.domain;

import com.bee.common.constants.Constants;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class DefaultRequest implements InvocationRequest{

    private static final long serialVersionUID = -6500608145417565251L;

    private byte serialize;
    private int callType = Constants.CALL_BACK_TYPE_REPLY;
    private int timeout = 0;
    private long createMillisTime;
    private String serviceName;
    private String methodName;
    private Object[] parameters;
    private int messageType = Constants.MESSAGE_TYPE_SERVICE;
    private Object context;
    private int size;
    private long seq;

    public DefaultRequest() {}

    public DefaultRequest(InvokerContext invokerContext) {
        if (invokerContext == null) return;
        this.methodName = invokerContext.getMethodName();
        this.messageType = Constants.MESSAGE_TYPE_SERVICE;
        this.parameters = invokerContext.getArguments();
        InvokerConfig<?> invokerConfig = invokerContext.getInvokerConfig();
        if (invokerConfig == null) return;
        this.serviceName = invokerConfig.getUrl();
        this.serialize = invokerConfig.getSerialize();
        this.timeout = invokerConfig.getTimeOut();
        if (Constants.CALL_ONEWAY.equalsIgnoreCase(invokerConfig.getCallType())) {
            this.callType = Constants.CALL_BACK_TYPE_NOREPLY;
        } else {
            this.callType = Constants.CALL_BACK_TYPE_REPLY;
        }
    }

    @Override
    public byte getSerialize() {
        return serialize;
    }

    @Override
    public void setSerialize(byte serialize) {
        this.serialize = serialize;
    }

    @Override
    public long getSeq() {
        return this.seq;
    }

    @Override
    public void setSeq(long seq) {
        this.seq = seq;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getCallType() {
        return callType;
    }

    @Override
    public void setCallType(int callType) {
        this.callType = callType;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public long getCreateMillisTime() {
        return createMillisTime;
    }

    @Override
    public void setCreateMillisTime(long createMillisTime) {
        this.createMillisTime = createMillisTime;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public int getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public String[] getParamClassName() {
        if (this.parameters == null) {
            return new String[0];
        }
        String[] paramClassNames = new String[this.parameters.length];

        int k = 0;
        for (Object parameter : this.parameters) {
            if (parameter == null) {
                paramClassNames[k] = "NULL";
            } else {
                paramClassNames[k] = parameter.getClass().getName();
            }
            k++;
        }
        return paramClassNames;
    }


    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }
}
