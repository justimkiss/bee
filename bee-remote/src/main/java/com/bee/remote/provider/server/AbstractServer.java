package com.bee.remote.provider.server;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.config.ServiceConfig;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.processor.RequestProcessor;
import com.bee.remote.provider.processor.RequestProcessorFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;

/**
 * Created by jeoy.zhou on 12/14/15.
 */
public abstract class AbstractServer implements Server {

    protected final Logger LOGGER = Logger.getLogger(this.getClass());
    protected ServiceConfig serviceConfig;
    protected RequestProcessor requestProcessor;

    public abstract void doStart(ServiceConfig serviceConfig);
    public abstract void doStop();
    public abstract <T> void doAddService(ProviderConfig<T> providerConfig);
    public abstract <T> void doRemoteService(ProviderConfig<T> providerConfig);

    @Override
    public void shutdown() {
        doStop();
    }

    @Override
    public void start(ServiceConfig serviceConfig) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info(String.format("%s: ready start, config: %s", this.getClass().getName(), serviceConfig));
        this.requestProcessor = RequestProcessorFactory.selectProcessor(serviceConfig);
        if (this.requestProcessor != null) {
            this.requestProcessor.start();
        }
        doStart(serviceConfig);
        this.serviceConfig = serviceConfig;
    }

    @Override
    public <T> void addService(ProviderConfig<T> providerConfig) {
        doAddService(providerConfig);
    }

    @Override
    public <T> void removeService(ProviderConfig providerConfig) {
        doRemoteService(providerConfig);
    }

    @Override
    public ServiceConfig getServiceConfig() {
        return this.serviceConfig;
    }

    @Override
    public boolean support(ServiceConfig serviceConfig) {
        if(serviceConfig == null) {
            throw new IllegalArgumentException(String.format("%s: serviceConfig is null", this.getClass().getName()));
        }
        return serviceConfig.getProtocol().equals(Protocol());
    }

    @Override
    public Future<InvocationResponse> processResponse(InvocationRequest request, ProviderContext providerContext) {
        return requestProcessor.processRequest(request, providerContext);
    }
}
