package com.bee.remote.provider.process.impl;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.AbstractProviderOperation;
import com.bee.remote.provider.domain.ProviderContext;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public class HeartBeatProviderOperation extends AbstractProviderOperation {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatProviderOperation.class);

    @Override
    public InvocationResponse invoke(ProviderContext invocationContext) throws Exception {
        return null;
    }
}
