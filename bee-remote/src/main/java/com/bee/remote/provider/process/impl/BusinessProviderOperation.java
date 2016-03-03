package com.bee.remote.provider.process.impl;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.AbstractProviderOperation;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.exception.RequestAbortedException;
import com.bee.remote.provider.process.interceptor.ProviderProcessInterceptor;
import com.bee.remote.provider.process.interceptor.ProviderProcessInterceptorFactory;
import com.bee.remote.provider.service.method.ServiceMethodDesc;
import com.bee.remote.provider.service.method.ServiceMethodFactory;
import com.bee.remote.provider.utils.ProviderUtils;
import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public class BusinessProviderOperation extends AbstractProviderOperation{

    private static final Logger LOGGER = Logger.getLogger(BusinessProviderOperation.class);

    private static final List<ProviderProcessInterceptor> INTERCEPTORS = ProviderProcessInterceptorFactory.getInterceptors();

    @Override
    public InvocationResponse invoke(ProviderContext providerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the BusinessProviderOperation, providerContext:" + providerContext);
        InvocationRequest request = providerContext.getRequest();
        if (request.getMessageType() != Constants.MESSAGE_TYPE_SERVICE)
            throw new InvalidParameterException("message type[" + request.getMessageType() + "] is not supported!");
        if (Thread.currentThread().isInterrupted())
            throw new RequestAbortedException("the request has been canceled by timeout checking processor: " + request);
        for (ProviderProcessInterceptor providerProcessInterceptor : INTERCEPTORS) {
            providerProcessInterceptor.preInvoke(request);
        }
        InvocationResponse response = null;
        ServiceMethodDesc serviceMethodDesc = providerContext.getServiceMethod();
        if (serviceMethodDesc == null)  serviceMethodDesc = ServiceMethodFactory.getMethod(request);
        Object result = serviceMethodDesc.invoke(request.getParameters());
        if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY)
            response = ProviderUtils.createSuccessResponse(request, result);
        return response;
    }
}
