package com.bee.remote.invoker;

import com.bee.register.RegisterManager;
import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.invoker.process.InvokerProcessHandlerFactory;
import com.bee.remote.invoker.processor.ResponseProcessorFactory;
import com.bee.remote.invoker.service.ServiceInvocationRepository;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public class InvokerBootStrap {

    private static final Logger LOGGER = Logger.getLogger(InvokerBootStrap.class);
    private static boolean isStart = false;

    public static boolean isStart() {
        return isStart;
    }

    public static void start() {
        if(isStart) return;
        synchronized (InvokerBootStrap.class) {
            if(isStart) return;
            RegisterManager.getInstance();
            ServiceInvocationRepository.getInstance().init();
            InvokerProcessHandlerFactory.init();
            SerializerFactory.init();
            isStart = true;
        }
    }

    public static void destroy() throws Exception {
        if (isStart) {
            synchronized (InvokerBootStrap.class) {
                if (isStart) {
                    try {
                        ClientManager.getInstance().destroy();
                    } catch (Exception e) {
                        LOGGER.error("clientManager destroy error", e);
                    }
                    try {
                        ServiceInvocationRepository.getInstance().destroy();
                    } catch (Exception e) {
                        LOGGER.error("ServiceInvocationRepository destroy error", e);
                    }
                    try {
                        ResponseProcessorFactory.shutdown();
                    } catch (Exception e) {
                        LOGGER.error("ResponseProcessorFactory destroy error", e);
                    }
                    isStart = false;
                }
            }
        }
    }
}
