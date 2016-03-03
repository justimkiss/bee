package com.bee.remote.provider.domain;

import com.bee.remote.common.codec.domain.InvocationContext;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.provider.service.method.ServiceMethodDesc;

import java.util.concurrent.Future;

/**
 * Created by jeoy.zhou on 1/31/16.
 */
public interface ProviderContext extends InvocationContext{

    public Throwable getServiceError();

    public void setServiceError(Throwable e);

    public ProviderChannel getChannel();

    public void setChannel(ProviderChannel channel);

    public Future<? extends InvocationResponse> getFuture();

    public void setFuture(Future<? extends  InvocationResponse> future);

    public Thread getThread();

    public void setThread(Thread thread);

    public ServiceMethodDesc getServiceMethod();

    public void setServiceMethod(ServiceMethodDesc serviceMethod);

}
