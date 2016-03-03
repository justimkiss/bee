package com.bee.remote.provider.domain;

import com.bee.remote.common.codec.domain.AbstractInvocationContext;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.provider.service.method.ServiceMethodDesc;

import java.util.concurrent.Future;

/**
 * Created by jeoy.zhou on 3/1/16.
 */
public class DefaultProviderContext extends AbstractInvocationContext implements ProviderContext{

    private Throwable serviceError;
    private ProviderChannel channel;
    private Future<? extends InvocationResponse> future;
    private Thread thread;
    private ServiceMethodDesc serviceMethod;

    public DefaultProviderContext(InvocationRequest request, ProviderChannel channel) {
        super(request);
        this.channel = channel;
    }

    @Override
    public ProviderChannel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(ProviderChannel channel) {
        this.channel = channel;
    }

    @Override
    public Throwable getServiceError() {
        return serviceError;
    }

    @Override
    public void setServiceError(Throwable serviceError) {
        this.serviceError = serviceError;
    }

    @Override
    public Future<? extends InvocationResponse> getFuture() {
        return future;
    }

    @Override
    public void setFuture(Future<? extends InvocationResponse> future) {
        this.future = future;
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public ServiceMethodDesc getServiceMethod() {
        return serviceMethod;
    }

    @Override
    public void setServiceMethod(ServiceMethodDesc serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}
