package com.bee.remote.provider.service.method;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.exception.InvocationFailureException;
import com.bee.remote.provider.service.ServiceProviderFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/10/15.
 */
public final class ServiceMethodFactory {

    private static final Logger LOGGER = Logger.getLogger(ServiceMethodFactory.class);
    private static Map<String, ServiceMethodCache> METHOD_CACHE_MAP = new ConcurrentHashMap<String, ServiceMethodCache>();


    /**
     * 通过request参数查找对应的method方法
     * @param request
     * @return
     * @throws InvocationFailureException
     */
    public static ServiceMethodDesc getMethod(InvocationRequest request) throws InvocationFailureException {
        String serviceName = request.getServiceName();
        String methodName = request.getMethodName();
        if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(methodName))
            throw new IllegalArgumentException("serviceName or methodName is required");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("getMethod by serviceName[%s] or methodName [%s]", serviceName, methodName));
        ServiceMethodCache serviceMethodCache = getServiceMethodCache(null, serviceName);
        if (serviceMethodCache == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("no service found by url: " + serviceName);
            }
            throw new InvocationFailureException("cannot find service for request:" + request);
        }
        return serviceMethodCache.getMethod(methodName, new ServiceParamDesc(request.getParamClassName()));

    }

    public static ServiceMethodCache getServiceMethodCache(ProviderConfig<?> providerConfig, String url) {
        if(providerConfig == null && StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("ServiceMethodFactory: getServiceMethodCache providerConfig and url all url");
        }
        String key = StringUtils.isBlank(url) ? providerConfig.getUrl() : url;
        ServiceMethodCache serviceMethodCache = null;
        if((serviceMethodCache = METHOD_CACHE_MAP.get(key)) == null) {
            ProviderConfig<?> providerConf = StringUtils.isBlank(url) ? providerConfig : ServiceProviderFactory.getProviderConfig(url);
            if(providerConf != null) {
                serviceMethodCache = new ServiceMethodCache(providerConf.getService());
            }
        }
        return  serviceMethodCache;
    }
    public static void registerServiceMethod(String url) {
        getServiceMethodCache(null, url);
    }

    public static void registerServiceMethod(ProviderConfig<?> providerConfig) {
        getServiceMethodCache(providerConfig, null);
    }

}
