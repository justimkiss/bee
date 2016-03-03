package com.bee.register.listen;

import com.bee.register.listen.event.ProviderChangeEnum;
import com.bee.register.listen.event.ServiceProviderChangeEvent;
import com.bee.register.listen.listener.ServiceProviderChangeListener;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class RegisterListenManager {

    private static final Logger LOGGER = Logger.getLogger(RegisterListenManager.class);

    private static final List<ServiceProviderChangeListener> SERVICE_PROVIDER_CHANGE_LISTENER_LIST = Lists.newArrayList();

    /**
     * 添加服务监听
     * @param listener
     */
    public static synchronized void addListener(ServiceProviderChangeListener listener) {
        SERVICE_PROVIDER_CHANGE_LISTENER_LIST.add(listener);
    }

    /**
     * 移除服务监听
     * @param listener
     * @return
     */
    public static synchronized boolean removeListener(ServiceProviderChangeListener listener) {
        return SERVICE_PROVIDER_CHANGE_LISTENER_LIST.remove(listener);
    }

    /**
     * 服务信息变动处理
     * @param serviceName
     * @param host
     * @param port
     * @param providerChangeEnum
     */
    public static void providerChanged(String serviceName, String host, int port, ProviderChangeEnum providerChangeEnum) {
        List<ServiceProviderChangeListener> listeners = Lists.newArrayList(SERVICE_PROVIDER_CHANGE_LISTENER_LIST);
        for(ServiceProviderChangeListener listener : listeners) {
            listener.providerChange(new ServiceProviderChangeEvent(host, port, serviceName, providerChangeEnum));
        }
    }


}
