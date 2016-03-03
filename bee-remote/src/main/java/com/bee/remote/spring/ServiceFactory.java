package com.bee.remote.spring;

import com.bee.common.entity.UrlConfig;
import com.bee.common.exception.RpcException;
import com.bee.common.util.UrlUtils;
import com.bee.config.ConfigManager;
import com.bee.config.loader.ConfigManagerLoader;
import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.invoker.ClientManager;
import com.bee.remote.invoker.InvokerBootStrap;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.provider.ProviderBootStrap;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.service.ServiceProviderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public class ServiceFactory {

    private static final Logger LOGGER = Logger.getLogger(ServiceFactory.class);
    private static final ConfigManager CONFIG_MANAGER = ConfigManagerLoader.getConfigManager();
    private static final Map<InvokerConfig<?>, Object> INVOKER_SERVICES = new ConcurrentHashMap<InvokerConfig<?>, Object>();

    static {
         try {
             ProviderBootStrap.init();
         } catch (Exception e) {
             e.printStackTrace();
             LOGGER.error("ServiceFactory: error while initializing service factory", e);
             System.exit(1);
         }
    }

    public static Map<InvokerConfig<?>, Object> getAllInvokersServices() {
        return INVOKER_SERVICES;
    }

    /**
     * 添加提供的service
     * @param providerConfig
     */
    public static void addService(ProviderConfig<?> providerConfig) {
        if(providerConfig == null) {
            LOGGER.error("ServiceFactory: no provider config");
            return;
        }
        LOGGER.info("ServiceFactory: add Service ==> " + providerConfig);
        try {
            ServiceFactory.verify(providerConfig);
            ServiceProviderFactory.addService(providerConfig);
            ProviderBootStrap.start(providerConfig);
            ServiceProviderFactory.publishService(providerConfig, true);
        } catch (Exception e) {
            throw new RpcException("occur errors while add service", e);
        }
    }

    /**
     * 添加提供服务的services
     * @param providerConfigs
     */
    public static void addService(List<ProviderConfig<?>> providerConfigs) {
        if(CollectionUtils.isEmpty(providerConfigs)) {
            LOGGER.warn("no providerConfigs");
            return;
        }
        for(ProviderConfig<?> config : providerConfigs) {
            addService(config);
        }
    }

    public static <T> T getService(InvokerConfig<T> invokerConfig) {
        if(invokerConfig == null)
            throw new IllegalArgumentException("invokerConfig is null");
        invokerConfig = configInvoker(invokerConfig);
        Object service = INVOKER_SERVICES.get(invokerConfig);
        if(service == null) {
            try {
                InvokerBootStrap.start();
                service = SerializerFactory.getSerializer(invokerConfig.getSerialize()).getProxy(invokerConfig);
            } catch (Exception e) {
                throw new RpcException("error while trying to get service", e);
            }
            try {
                ClientManager.getInstance().registerClient(invokerConfig.getUrl());
            } catch (Exception e) {
                LOGGER.warn("error while trying to setup service client:" + invokerConfig, e);
            }
            INVOKER_SERVICES.put(invokerConfig, service);
        }
        return (T) service;
    }

    public static void closeAllServices() {

    }

    /**
     * 解析invokerConfig中的版本信息
     * @param invokerConfig
     * @return
     */
    private static <T> InvokerConfig<T> configInvoker(InvokerConfig<T> invokerConfig) {
        if(StringUtils.isBlank(invokerConfig.getUrl())) {
            invokerConfig.setUrl(invokerConfig.getServiceInterface().getCanonicalName());
        } else {
            UrlConfig urlConfig = UrlUtils.getUrlConfig(invokerConfig.getUrl());
            invokerConfig.setVersion(urlConfig.getVersion());
        }
        return invokerConfig;
    }

    /**
     * 验证providerConfig各个属性的合法性
     * 如果验证失败抛出RpcException异常
     * @param providerConfig
     */
    private static void verify(ProviderConfig providerConfig) {
        if(providerConfig == null) {
            throw new RpcException("providerConfig is null");
        }
        if(StringUtils.isBlank(providerConfig.getUrl())) {
            throw new RpcException("providerConfig attribute url is empty");
        }
        if(providerConfig.getService() == null) {
            throw new RpcException("providerConfig attribute service is null");
        }
        if(providerConfig.getServiceConfig() == null) {
            throw new RpcException("providerConfig attribute serviceConfig is null");
        }
        if(providerConfig.getServiceInterface() == null) {
            throw new RpcException("providerConfig attribute serviceInterface is null");
        }
    }
}
