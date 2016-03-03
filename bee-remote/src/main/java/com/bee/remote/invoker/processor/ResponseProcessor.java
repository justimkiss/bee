package com.bee.remote.invoker.processor;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public interface ResponseProcessor {

    public void processResponse(InvocationResponse invocationResponse, Client client);

    public void shutdown();
}
