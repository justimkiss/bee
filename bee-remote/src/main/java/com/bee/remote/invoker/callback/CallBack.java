package com.bee.remote.invoker.callback;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public interface CallBack extends Call {

    public void callBack(InvocationResponse invocationResponse);

    public void setRequest(InvocationRequest invocationRequest);

    public void completed();

}
