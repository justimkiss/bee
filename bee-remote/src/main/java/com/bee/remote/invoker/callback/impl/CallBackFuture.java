package com.bee.remote.invoker.callback.impl;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.exception.NetTimeoutException;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.callback.CallFuture;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public class CallBackFuture implements CallBack, CallFuture {

    private static final Logger LOGGER = Logger.getLogger(CallBackFuture.class);

    protected InvocationResponse response;
    protected volatile boolean done;
    protected volatile boolean canceled;
    protected volatile boolean success;
    protected InvocationRequest request;
    protected Client client;

    protected Object object = new Object();

    @Override
    public void callBack(InvocationResponse invocationResponse) {
        this.response = invocationResponse;
    }

    @Override
    public void setRequest(InvocationRequest invocationRequest) {
        this.request = request;
    }

    @Override
    public void completed() {
        synchronized (object) {
            this.done = true;
            if (this.response.getMessageType() == Constants.MESSAGE_TYPE_SERVICE) {
                this.success = true;
            }
            this.notifyAll();
        }
    }

    @Override
    public InvocationResponse get() throws InterruptedException {
        return get(Long.MAX_VALUE);
    }

    @Override
    public InvocationResponse get(long timeoutMillis) throws InterruptedException {
        synchronized (object) {
            long start = System.currentTimeMillis();
            while (!this.done) {
                long timeoutLimit = timeoutMillis - (System.currentTimeMillis() - start);
                if (timeoutLimit < 0) {
                    throw new NetTimeoutException(
                            String.format("request timeout, current time: %d \r\nrequest: %s\r\nhost: %s",
                                    System.currentTimeMillis(), this.request, this.client.getAddress()));
                } else {
                    object.wait();
                }
            }
            // TODO
            // processContext()
            if (response.getMessageType() == Constants.MESSAGE_TYPE_EXCEPTION) {
                LOGGER.error(String.format("remote call exception\r\nrequest: %s\r\nhost: %s\r\nresponse: %s",
                        this.request, this.client.getAddress(), this.response));
            }
        }
        return this.response;
    }

    @Override
    public InvocationResponse get(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return get(timeUnit.toMillis(timeout));
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public boolean isCancel() {
        return this.canceled;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Client getClient() {
        return this.client;
    }

}
