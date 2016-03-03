package com.bee.remote.provider.processor;

import com.bee.remote.provider.config.ServiceConfig;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 3/1/16.
 */
public final class RequestProcessorFactory {

    private static final Logger LOGGER = Logger.getLogger(RequestProcessorFactory.class);

    public static RequestProcessor selectProcessor(ServiceConfig serviceConfig) {
        return new RequestThreadPoolProcessor(serviceConfig);
    }

}
