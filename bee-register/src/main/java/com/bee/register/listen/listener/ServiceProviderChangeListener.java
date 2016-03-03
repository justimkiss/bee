package com.bee.register.listen.listener;

import com.bee.register.listen.event.ServiceProviderChangeEvent;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public interface ServiceProviderChangeListener {

    public void providerChange(ServiceProviderChangeEvent event);
}
