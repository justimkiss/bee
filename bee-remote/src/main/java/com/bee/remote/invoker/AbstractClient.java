package com.bee.remote.invoker;

import com.bee.common.exception.NetworkException;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.processor.ResponseProcessor;
import com.bee.remote.invoker.processor.ResponseProcessorFactory;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public abstract class AbstractClient implements Client{

    private static final ResponseProcessor RESPONSE_PROCESSOR = ResponseProcessorFactory.getProcessor();

    @Override
    public InvocationResponse sendRequest(InvocationRequest request) throws NetworkException {
        return sendRequest(request, null);
    }

    @Override
    public InvocationResponse sendRequest(InvocationRequest request, CallBack callback) throws NetworkException {
        // 暂时不做处理
        return doSendRequest(request, callback);
    }

    @Override
    public void processResponse(InvocationResponse invocationResponse) {
        RESPONSE_PROCESSOR.processResponse(invocationResponse, this);
    }

    public abstract InvocationResponse doSendRequest(InvocationRequest request, CallBack callback) throws NetworkException;

}
