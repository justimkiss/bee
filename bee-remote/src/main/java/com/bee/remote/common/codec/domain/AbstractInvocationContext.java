package com.bee.remote.common.codec.domain;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public abstract class AbstractInvocationContext implements InvocationContext{

    private InvocationRequest request;
    private InvocationResponse response;

    public AbstractInvocationContext() {}

    public AbstractInvocationContext(InvocationRequest request) {
        this.request = request;
    }

    @Override
    public InvocationRequest getRequest() {
        return request;
    }

    @Override
    public void setRequest(InvocationRequest request) {
        this.request = request;
    }

    @Override
    public InvocationResponse getResponse() {
        return response;
    }

    public void setResponse(InvocationResponse response) {
        this.response = response;
    }
}
