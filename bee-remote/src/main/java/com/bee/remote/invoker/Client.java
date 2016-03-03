package com.bee.remote.invoker;

import com.bee.common.exception.NetworkException;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.domain.ConnectInfo;


/**
 * Created by jeoy.zhou on 12/27/15.
 */
public interface Client {

    public boolean isConnected();

    public ConnectInfo getConnectInfo();

    public void connect();

    public boolean isActive();

    public void setActive(boolean isActive);

    public String getAddress();

    public void close();

    public InvocationResponse sendRequest(InvocationRequest request) throws NetworkException;

    public InvocationResponse sendRequest(InvocationRequest request, CallBack callback) throws NetworkException;

    public void processResponse(InvocationResponse invocationResponse);
}
