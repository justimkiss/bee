package com.bee.remote.invoker.processor;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public abstract class AbstractResponseProcessor implements ResponseProcessor{

    private static final Logger LOGGER = Logger.getLogger(AbstractResponseProcessor.class);


    @Override
    public void processResponse(InvocationResponse invocationResponse, Client client) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("processResponse: " + invocationResponse);
        try {
            doProcessProcess(invocationResponse, client);
        } catch (Exception e) {
            LOGGER.error("processResponse error", e);
        }
    }

    protected abstract void doProcessProcess(InvocationResponse invocationResponse, Client client);

}
