package com.bee.remote.common.codec.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 动态代理处理返回结果
 * Created by jeoy.zhou on 1/31/16.
 */
public interface InvocationResponse extends InvocationSerializable {

    public void setMessageType(int messageType);

    public int getMessageType();

    public String getCause();

    public Object getReturn();

    public void setReturn(Object obj);

    public Map<String, Serializable> getResponseValues();

    public void setResponseValues(Map<String, Serializable> responseValues);
}
