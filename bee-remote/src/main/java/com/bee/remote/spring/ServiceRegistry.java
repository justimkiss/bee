package com.bee.remote.spring;

import com.bee.common.constants.Constants;
import com.bee.common.exception.RpcException;
import com.bee.common.util.NetUtils;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.config.ServiceConfig;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jeoy.zhou on 12/3/15.
 */
public class ServiceRegistry {

    private static final Logger LOGGER = Logger.getLogger(ServiceRegistry.class);
    private Map<String, Object> services;
    private Integer port;
    private int corePoolSize = Constants.DEFAULT_PROVIDER_COREPOOLSIZE;
    private int maxPoolSize = Constants.DEFAULT_PROVIDER_MAXPOOLSIZE;
    private int workQueueSize = Constants.DEFAULT_PROVIDER_WORKQUEUESIZE;

    public void init() {
        if(MapUtils.isEmpty(services)) {
            throw new RpcException("ServiceRegistry: services is empty");
        }
        ServiceConfig serviceConfig = new ServiceConfig();
        if(port == null || port <= 1024 && port > 65535) {
            throw new IllegalArgumentException(String.format("ServiceRegistry: service port[%s] is Illegal, port must in(1025-65535)", port));
        } else {
            serviceConfig.setPort(port);
        }
        serviceConfig.setCorePoolSize(corePoolSize);
        serviceConfig.setMaxPoolSize(maxPoolSize);
        serviceConfig.setWorkQueueSize(workQueueSize);
        String localIP = NetUtils.getLocalIP();
        if(StringUtils.isBlank(localIP)) {
            throw new IllegalArgumentException(String.format("ServiceRegistry: can not get local IP"));
        } else {
            serviceConfig.setIp(localIP);
        }
        List<ProviderConfig<?>> providerConfigs = new ArrayList<ProviderConfig<?>>();
        ProviderConfig<?> providerConfig = null;
        for(String url : services.keySet()) {
            providerConfig = new ProviderConfig<Object>(url, services.get(url));
            providerConfig.setServiceConfig(serviceConfig);
            providerConfigs.add(providerConfig);
        }
        ServiceFactory.addService(providerConfigs);
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    public Map<String, Object> getServices() {
        return services;
    }

    public void setServices(Map<String, Object> services) {
        this.services = services;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
