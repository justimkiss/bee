package com.bee.remote.invoker.callback.sync;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.callback.impl.CallBackFuture;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public class ServiceFutureImpl extends CallBackFuture implements ServiceFuture {

    private static final Logger LOGGER = Logger.getLogger(ServiceFutureImpl.class);

    private long timeout = Long.MAX_VALUE;

    public ServiceFutureImpl() {
        super();
    }

    public ServiceFutureImpl(long timeoutMills) {
        this();
        this.timeout = timeoutMills;
    }

    @Override
    public void completed() {
        // nothing to do
    }

    @Override
    public Object getResult() throws InterruptedException {
        return getResult(timeout);
    }

    @Override
    public Object getResult(long timeoutMillis) throws InterruptedException {
        InvocationResponse response = null;
        try {
            response = super.get(timeoutMillis);
        } catch (Exception e) {
            LOGGER.error("serviceFuture error", e);
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public Object getResult(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return getResult(timeUnit.toMillis(timeout));
    }
}
