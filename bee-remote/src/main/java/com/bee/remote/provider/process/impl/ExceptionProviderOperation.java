package com.bee.remote.provider.process.impl;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.AbstractProviderOperation;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.utils.ProviderUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class ExceptionProviderOperation extends AbstractProviderOperation {

    private static final Logger LOGGER = Logger.getLogger(ExceptionProviderOperation.class);

    @Override
    public InvocationResponse invoke(ProviderContext providerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the ExceptionProviderOperation, providerContext:" + providerContext);
        InvocationRequest request = providerContext.getRequest();
        InvocationResponse response = null;
        try {
            response = getProviderOperation().invoke(providerContext);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException != null) {
                LOGGER.error(targetException.getMessage(), targetException);
            }
            if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY)
                response = ProviderUtils.createServiceExceptionResponse(request, targetException);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            providerContext.setServiceError(e);
            if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY
                    && request.getMessageType() != Constants.MESSAGE_TYPE_HEART)
                response = ProviderUtils.createFailResponse(request, e);
        }
        return response;
    }
}
