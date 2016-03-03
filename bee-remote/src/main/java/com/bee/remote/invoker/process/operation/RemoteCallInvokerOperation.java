package com.bee.remote.invoker.process.operation;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.callback.impl.CallBackFuture;
import com.bee.remote.invoker.callback.sync.ServiceFutureFactory;
import com.bee.remote.invoker.callback.sync.ServiceFutureImpl;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;
import com.bee.remote.invoker.utils.InvokerUtils;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public class RemoteCallInvokerOperation extends InvocationInvokerOperation {

    private static final Logger LOGGER = Logger.getLogger(RemoteCallInvokerOperation.class);

    private static final InvocationResponse RESPONSE = InvokerUtils.createNoReturnResponse();

    @Override
    public InvocationResponse invoke(InvokerContext invokerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the RemoteCallInvokeFilter, invocationContext:" + invokerContext);
        Client client = invokerContext.getClient();
        InvocationRequest request = invokerContext.getRequest();
        InvokerConfig<?> invokerConfig = invokerContext.getInvokerConfig();
        String callType = invokerConfig.getCallType();
        InvocationResponse response = null;
        int timeOut = request.getTimeout();
        if (Constants.CALL_SYNC.equalsIgnoreCase(callType)) {
            CallBackFuture callBackFuture = new CallBackFuture();
            response = InvokerUtils.sendRequest(client, request, callBackFuture);
            if (response == null)
                response = callBackFuture.get(timeOut);
        } else if (Constants.CALL_FUTURE.equalsIgnoreCase(callType)) {
            ServiceFutureImpl serviceFuture = new ServiceFutureImpl();
            InvokerUtils.sendRequest(client, request, serviceFuture);
            ServiceFutureFactory.setFuture(serviceFuture);
            response = InvokerUtils.createFutureResponse(serviceFuture);
        } else if (Constants.CALL_ONEWAY.equalsIgnoreCase(callType)) {
            InvokerUtils.sendRequest(client, request, null);
            return RESPONSE;
        } else {
            throw new IllegalArgumentException("Call type[" + callType + "] is not supported!");
        }
        return response;
    }
}
