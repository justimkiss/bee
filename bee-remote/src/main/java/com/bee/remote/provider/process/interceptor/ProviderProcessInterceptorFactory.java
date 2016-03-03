package com.bee.remote.provider.process.interceptor;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public final class ProviderProcessInterceptorFactory {

    private static final Logger LOGGER = Logger.getLogger(ProviderProcessInterceptorFactory.class);

    private static final List<ProviderProcessInterceptor> INTERCEPTORS = new ArrayList<ProviderProcessInterceptor>();

    public static List<ProviderProcessInterceptor> getInterceptors() {
        return INTERCEPTORS;
    }

    public static boolean registerInterceptor(ProviderProcessInterceptor providerProcessInterceptor) {
        if (INTERCEPTORS.contains(providerProcessInterceptor)) return false;
        return INTERCEPTORS.add(providerProcessInterceptor);
    }

    public static boolean unRegisterInterceptor(ProviderProcessInterceptor providerProcessInterceptor) {
        return INTERCEPTORS.remove(providerProcessInterceptor);
    }
}
