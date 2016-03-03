package com.bee.remote.invoker.callback.sync;

import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public interface ServiceFuture {

    public Object getResult() throws InterruptedException;

    public Object getResult(long timeoutMillis) throws InterruptedException;

    public Object getResult(long timeout, TimeUnit timeUnit) throws InterruptedException;

}
