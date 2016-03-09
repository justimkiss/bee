package com.bee.remote.provider.process.impl;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.AbstractProviderOperation;
import com.bee.remote.provider.domain.ProviderChannel;
import com.bee.remote.provider.domain.ProviderContext;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public class WriteResponseProviderOperation extends AbstractProviderOperation {

    private static final Logger LOGGER = Logger.getLogger(WriteResponseProviderOperation.class);

    @Override
    public InvocationResponse invoke(ProviderContext providerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the WriteResponseProviderOperation, providerContext:" + providerContext);
        ProviderChannel channel = providerContext.getChannel();
        InvocationRequest request = providerContext.getRequest();
        InvocationResponse response = getProviderOperation().invoke(providerContext);
        if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY) {
            channel.write(response);
        }
        return response;
    }
}
