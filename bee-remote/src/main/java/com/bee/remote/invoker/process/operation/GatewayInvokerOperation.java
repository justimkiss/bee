package com.bee.remote.invoker.process.operation;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.domain.InvokerContext;
import com.bee.remote.invoker.route.statics.InvokerStaticsHolder;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public class GatewayInvokerOperation extends InvocationInvokerOperation{

    private static final Logger LOGGER = Logger.getLogger(GatewayInvokerOperation.class);

    @Override
    public InvocationResponse invoke(InvokerContext invocationContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the GatewayInvokerOperation, invocationContext:" + invocationContext);
        InvokerStaticsHolder.flowIn(invocationContext);
        return getServiceInvocationOperation().invoke(invocationContext);
    }
}
