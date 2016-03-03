package com.bee.remote.invoker.domain;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.callback.CallBack;


/**
 * Created by jeoy.zhou on 2/24/16.
 */
public final class RemoteInvocationBean {

    private CallBack callback;
    private InvocationRequest request;

    public RemoteInvocationBean(InvocationRequest request, CallBack callback) {
        this.request = request;
        this.callback = callback;
    }

    public CallBack getCallback() {
        return callback;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public InvocationRequest getRequest() {
        return request;
    }

    public void setRequest(InvocationRequest request) {
        this.request = request;
    }
}
