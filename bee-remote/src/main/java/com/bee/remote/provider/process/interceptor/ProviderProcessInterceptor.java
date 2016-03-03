package com.bee.remote.provider.process.interceptor;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public interface ProviderProcessInterceptor {

    public void preInvoke(InvocationRequest request);

    public void postInvoke(InvocationRequest request, InvocationResponse response);
}
