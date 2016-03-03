package com.bee.remote.provider.process.impl;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.domain.Disposable;
import com.bee.remote.common.process.filter.AbstractProviderOperation;
import com.bee.remote.provider.domain.ProviderContext;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public class GatewayProviderOperation extends AbstractProviderOperation implements Disposable{

    private static final Logger LOGGER = Logger.getLogger(GatewayProviderOperation.class);

    @Override
    public InvocationResponse invoke(ProviderContext providerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the GatewayProviderOperation, invocationContext:" + providerContext);
        // TODO 到时候添加流量限制
        return getProviderOperation().invoke(providerContext);
    }

    @Override
    public void destroy() throws Exception {

    }
}
