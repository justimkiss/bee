package com.bee.remote.provider.service;

import com.bee.common.constants.Constants;
import com.bee.common.util.VersionUtils;
import com.bee.config.ConfigManager;
import com.bee.config.loader.ConfigManagerLoader;
import com.bee.register.RegisterManager;
import com.bee.remote.provider.ProviderBootStrap;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.server.Server;
import com.bee.remote.provider.service.method.ServiceMethodFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public final class ServiceProviderFactory {

    private static final Logger LOGGER = Logger.getLogger(ServiceProviderFactory.class);
    private static final ConfigManager CONFIG_MANAGER = ConfigManagerLoader.getConfigManager();
    private static final Map<String, ProviderConfig<?>> serviceConfigCache = new ConcurrentHashMap<String, ProviderConfig<?>>();


    public static <T> void addService(ProviderConfig<T> providerConfig) throws Exception {
        LOGGER.info(String.format("ServiceProviderFactory: add Server[%s]", providerConfig));
        // confirm the default service by serviceName
        ProviderConfig<?> defaultProvider = null;
        if((defaultProvider = serviceConfigCache.get(providerConfig.getServiceName())) != null) {
            if(VersionUtils.compareVersion(providerConfig.getVersion(), defaultProvider.getVersion()) > 0) {
                serviceConfigCache.put(providerConfig.getServiceName(), providerConfig);
            }
        } else {
            serviceConfigCache.put(providerConfig.getServiceName(), providerConfig);
        }
        serviceConfigCache.put(providerConfig.getUrl(), providerConfig);
        T service = providerConfig.getService();
        if(service instanceof BeeInitialize) {
            ((BeeInitialize) service).init();
        }
        ServiceMethodFactory.registerServiceMethod(providerConfig);
    }

    /**
     * 发布服务
     * @param providerConfig
     */
    public static void publishService(ProviderConfig<?> providerConfig, boolean isUseCache) {
        String url = providerConfig.getUrl();
        ProviderConfig<?> providerConfigCache = serviceConfigCache.get(url);
        if(providerConfigCache == null) {
            LOGGER.warn(String.format("ServiceProviderFactory: not find providerConfig by url[%s]", url));
            return;
        }
        if(isUseCache) {
            providerConfig = providerConfigCache;
        }
        List<Server> servers = ProviderBootStrap.getServers(providerConfig);
        if(CollectionUtils.isEmpty(servers)) {
            LOGGER.warn("ServiceProviderFactory: no server to provider");
            return;
        }
        String serverAddress = null;
        for(Server server : servers) {
            serverAddress = server.getServiceConfig().getIp() +
                    Constants.COLON_SYMBOL + server.getServiceConfig().getPort();
            RegisterManager.getInstance().registerService(providerConfig.getUrl(), serverAddress, Constants.DEFAULT_WEIGHT);
        }
    }


    /**
     * get serviceProvider
     * @param url
     * @return
     */
    public static ProviderConfig<?> getProviderConfig(String url) {
        return serviceConfigCache.get(url);
    }

}
