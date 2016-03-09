package com.bee.remote.invoker.cluster;

import com.bee.common.constants.Constants;
import com.bee.common.exception.NetworkException;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.ServiceInvocationOperation;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.ClientManager;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;
import com.bee.remote.invoker.exception.ServiceUnavailableException;
import com.bee.remote.invoker.utils.InvokerUtils;
import org.apache.commons.lang.SerializationException;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/17/16.
 */
public class FastFailCluster implements Cluster {

    private static final Logger LOGGER = Logger.getLogger(FastFailCluster.class);
    private static final ClientManager CLIENT_MANAGER = ClientManager.getInstance();

    @Override
    public String getName() {
        return Constants.CLUSTER_FAILFAST;
    }

    @Override
    public InvocationResponse invoke(ServiceInvocationOperation serviceInvocationOperation, InvokerContext invokerContext) throws Exception {
        InvokerConfig<?> invokerConfig = invokerContext.getInvokerConfig();
        InvokerUtils.createRemoteCallRequest(invokerContext);
        int retry = 0;
        if (!invokerConfig.isTimeOutRetry())
            retry = 1;
        else
            retry = invokerConfig.getRetries();
        for (int index = 0; index < retry; index++) {
            try {
                Client remoteClient = CLIENT_MANAGER.getClient(invokerConfig, invokerContext);
                invokerContext.setClient(remoteClient);
                return serviceInvocationOperation.invoke(invokerContext);
            } catch (ServiceUnavailableException e) {
                throw e;
            } catch (NetworkException e) {
                if (index >= retry) {
                    throw e;
                }
                LOGGER.error("FastFailCluster: invoke error; retry time: " + (index + 1), e);
            }
        }
        throw new SerializationException("Invoke method[" + invokerContext.getMethodName()
                + "] on service[" + invokerConfig.getUrl() + "] failed ");
//        if (!invokerConfig.isTimeOutRetry()) {
//            Client remoteClient = CLIENT_MANAGER.getClient(invokerConfig, invokerContext);
//            invokerContext.setClient(remoteClient);
//            try {
//                return serviceInvocationOperation.invoke(invokerContext);
//            } catch (Exception e) {
//                LOGGER.error("FastFailCluster: invoke error", e);
//                throw e;
//            }
//        } else {
//            int retry = invokerConfig.getRetries();
//            for (int index = 0; index < retry; index++) {
//                try {
//                    Client remoteClient = CLIENT_MANAGER.getClient(invokerConfig, invokerContext);
//                    invokerContext.setClient(remoteClient);
//                    return serviceInvocationOperation.invoke(invokerContext);
//                } catch (Exception e) {
//                    LOGGER.error("FastFailCluster: invoke error; retry time: " + (index + 1), e);
//                }
//            }
//            throw new SerializationException("Invoke method[" + invokerContext.getMethodName()
//                    + "] on service[" + invokerConfig.getUrl() + "] failed ");
//        }
    }

}
