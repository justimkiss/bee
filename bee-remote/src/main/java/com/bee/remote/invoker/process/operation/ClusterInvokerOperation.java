package com.bee.remote.invoker.process.operation;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.cluster.Cluster;
import com.bee.remote.invoker.cluster.ClusterFactory;
import com.bee.remote.invoker.domain.InvokerContext;
import com.jeoy.bee.monitor.Monitor;
import com.jeoy.bee.monitor.MonitorManager;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public class ClusterInvokerOperation extends InvocationInvokerOperation{

    private static final Logger LOGGER = Logger.getLogger(ClusterInvokerOperation.class);
    private static final Monitor MONITOR = MonitorManager.getMonitor();

    @Override
    public InvocationResponse invoke(InvokerContext invokerContext) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the ClusterInvokerOperation, invocationContext:" + invokerContext);
        Cluster cluster = ClusterFactory.selectCluster(invokerContext);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("invoke the ClusterInvokerOperation, select cluster:" + cluster.getName());
        try {
            return cluster.invoke(getServiceInvocationOperation(), invokerContext);
        } catch (Exception e) {
            MONITOR.logError("invoke remote call failed", e);
            LOGGER.error("invoke remote call failed", e);
            throw e;
        }
    }

}
