package com.bee.remote.provider.config;

import com.bee.common.entity.UrlConfig;
import com.bee.common.util.UrlUtils;
import com.bee.config.ConfigManager;
import com.bee.config.loader.ConfigManagerLoader;
import com.bee.config.utills.InterfaceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public class ProviderConfig<T> {

    private static final Logger LOGGER = Logger.getLogger(ProviderConfig.class);

    /**
     * 提供服务service实体的接口
     */
    private Class<?> serviceInterface;
    /**
     * 提供服务的service
     */
    private T service;
    private String serviceName;
    private String url;
    /**
     * in current version, not use
      */
    private String version;
    private ServiceConfig serviceConfig;
    private boolean isPublish = false;

    private ConfigManager configManager = ConfigManagerLoader.getConfigManager();


    public ProviderConfig(String url, T service) {
        this(url, service, null);
    }

    public ProviderConfig(String url, T service, Class<?> serviceInterface){
        if(StringUtils.isBlank(url))
            throw new IllegalArgumentException("ProviderConfig: param[url] is null");
        if(serviceInterface == null) {
            serviceInterface = InterfaceUtils.getInterface(service.getClass());
        } else if(!serviceInterface.isInstance(service)) {
            throw new IllegalArgumentException(String.format("ProviderConfig: server [%s] not implement interface [%s]", service.getClass().getName(), serviceInterface.getName()));
        }
        setUrl(url);
        setServiceInterface(serviceInterface);
        setService(service);
        init();
    }

    private void init() {
//        parseUrl();
        UrlConfig urlConfig = UrlUtils.getUrlConfig(getUrl());
        this.serviceName = urlConfig.getServiceName();
        this.version = urlConfig.getVersion();
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean isPublish() {
        return isPublish;
    }

    public void setIsPublish(boolean isPublish) {
        this.isPublish = isPublish;
    }

//    private void parseUrl() {
//        if(StringUtils.isBlank(url)) {
//            throw new IllegalArgumentException("providerConfig attribute[url] is null");
//        }
//        Matcher matcher = Constants.BEE_SERVICE_URL_PATTERN.matcher(url);
//        if(!matcher.find() || matcher.groupCount() != 2) {
//            throw new IllegalArgumentException(String.format("providerConfig attribute[url:%s] is invalid", url));
//        }
//        this.version = matcher.group(2);
//        this.serviceName = matcher.group(1);;
//    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
