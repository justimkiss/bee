package com.bee.remote.provider.server;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.config.ServiceConfig;
import com.bee.remote.provider.domain.ProviderContext;

import java.util.concurrent.Future;

/**
 * Created by jeoy.zhou on 12/14/15.
 */
public interface Server {

    public void shutdown();

    public String Protocol();

    public boolean support(ServiceConfig serviceConfig);

    public void start(ServiceConfig serviceConfig);

    public <T> void addService(ProviderConfig<T> providerConfig);

    public <T> void removeService(ProviderConfig providerConfig);

    public ServiceConfig getServiceConfig();

    public boolean isStart();

    public Future<InvocationResponse> processResponse(InvocationRequest request, ProviderContext providerContext);
}
