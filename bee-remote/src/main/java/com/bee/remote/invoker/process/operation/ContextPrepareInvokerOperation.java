package com.bee.remote.invoker.process.operation;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public class ContextPrepareInvokerOperation extends InvocationInvokerOperation {

    private static final Logger LOGGER = Logger.getLogger(ContextPrepareInvokerOperation.class);

    @Override
    public InvocationResponse invoke(InvokerContext invokerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the ContextPrepareInvokerOperation, invocationContext:" + invokerContext);
        initRequest(invokerContext);
        return getServiceInvocationOperation().invoke(invokerContext);
    }

    /**
     * 初始化request
     * @param invokerContext
     */
    private void initRequest(InvokerContext invokerContext) {
        InvocationRequest request = invokerContext.getRequest();
        request.setCreateMillisTime(System.currentTimeMillis());
        request.setMessageType(Constants.MESSAGE_TYPE_SERVICE);
        InvokerConfig<?> invokerConfig = invokerContext.getInvokerConfig();
        if (invokerConfig != null) {
            if (Constants.CALL_ONEWAY.equalsIgnoreCase(invokerConfig.getCallType())) {
                request.setCallType(Constants.CALL_BACK_TYPE_NOREPLY);
            } else {
                request.setCallType(Constants.CALL_BACK_TYPE_REPLY);
            }
        }
    }
}
