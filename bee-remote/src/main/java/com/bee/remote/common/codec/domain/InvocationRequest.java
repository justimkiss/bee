package com.bee.remote.common.codec.domain;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public interface InvocationRequest extends InvocationSerializable{

    int getCallType();

    void setCallType(int callType);

    int getTimeout();

    void setTimeout(int timeout);

    long getCreateMillisTime();

    void setCreateMillisTime(long createMillisTime);

    String getServiceName();

    void setServiceName(String serviceName);

    String getMethodName();

    void setMethodName(String methodName);

    Object[] getParameters();

    String[] getParamClassName();

}
