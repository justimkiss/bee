package com.bee.remote.invoker.callback;

import com.bee.remote.common.codec.domain.InvocationResponse;

import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public interface CallFuture extends Call {

    public InvocationResponse get() throws InterruptedException;

    public InvocationResponse get(long timeoutMillis) throws InterruptedException;

    public InvocationResponse get(long timeout, TimeUnit timeUnit) throws InterruptedException;

    public boolean cancel();

    public boolean isCancel();

    public boolean isDone();
}
