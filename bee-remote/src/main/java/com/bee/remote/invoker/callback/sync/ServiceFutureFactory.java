package com.bee.remote.invoker.callback.sync;

import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public final class ServiceFutureFactory {

    private static final Logger LOGGER = Logger.getLogger(ServiceFutureFactory.class);

    private static ThreadLocal<ServiceFuture> THREAD_LOCAL = new ThreadLocal<ServiceFuture>();

    public static ServiceFuture getFuture() {
        ServiceFuture serviceFuture = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        return serviceFuture;
    }

    public static void setFuture(ServiceFuture serviceFuture) {
        THREAD_LOCAL.set(serviceFuture);
    }

    public static Object getResult() throws InterruptedException {
        return getFuture().getResult();
    }

    public static <T> T getResult(Class<T> clazz) throws InterruptedException {
        return (T) getFuture().getResult();
    }

}
