package com.bee.remote.invoker.cluster;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.ServiceInvocationOperation;
import com.bee.remote.invoker.domain.InvokerContext;

/**
 * Created by jeoy.zhou on 2/17/16.
 */
public interface Cluster {

    public String getName();

    public InvocationResponse invoke(ServiceInvocationOperation<InvokerContext> serviceInvocationOperation, InvokerContext invokerContext) throws Exception;
}
