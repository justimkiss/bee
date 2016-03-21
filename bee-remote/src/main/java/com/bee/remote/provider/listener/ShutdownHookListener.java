package com.bee.remote.provider.listener;

import com.bee.remote.invoker.InvokerBootStrap;
import com.bee.remote.provider.ProviderBootStrap;
import com.bee.remote.spring.ServiceFactory;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/11/15.
 */
public class ShutdownHookListener implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(ShutdownHookListener.class);

    @Override
    public void run() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("ShutdownHookListener: shutdown begin...");
        try {
            ServiceFactory.closeAllServices();
        } catch (Exception e) {
            LOGGER.error("ShutdownHookListener: ServiceFactory.closeAllServices error", e);
        }
        try {
            InvokerBootStrap.destroy();
        } catch (Exception e) {
            LOGGER.error("ShutdownHookListener: InvokerBootStrap.destroy error", e);
        }
        try {
            ProviderBootStrap.shutdown();
        } catch (Exception e) {
            LOGGER.error("error with shutdown hook", e);
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("ShutdownHookListener: shutdown end...");
    }
}
