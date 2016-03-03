package com.bee.remote.common.codec.domain;

/**
 * 动态代理入参
 * Created by jeoy.zhou on 1/31/16.
 */
public interface InvocationContext {

    public InvocationRequest getRequest();

    public void setRequest(InvocationRequest invocationRequest);

    public InvocationResponse getResponse();
}
