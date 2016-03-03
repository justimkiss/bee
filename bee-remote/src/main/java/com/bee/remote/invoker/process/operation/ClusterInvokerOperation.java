package com.bee.remote.invoker.process.operation;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.cluster.Cluster;
import com.bee.remote.invoker.cluster.ClusterFactory;
import com.bee.remote.invoker.domain.InvokerContext;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public class ClusterInvokerOperation extends InvocationInvokerOperation{

    private static final Logger LOGGER = Logger.getLogger(ClusterInvokerOperation.class);

    @Override
    public InvocationResponse invoke(InvokerContext invokerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the ClusterInvokerOperation, invocationContext:" + invokerContext);
        Cluster cluster = ClusterFactory.selectCluster(invokerContext);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the ClusterInvokerOperation, select cluster:" + cluster.getName());
        return cluster.invoke(getServiceInvocationOperation(), invokerContext);
    }

}
