package com.bee.remote.invoker.process;

import com.bee.remote.common.process.filter.ServiceInvocationOperation;
import com.bee.remote.invoker.process.operation.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeoy.zhou on 1/31/16.
 */
public final class InvokerProcessHandlerFactory {

    private final static Logger LOGGER = Logger.getLogger(InvokerProcessHandlerFactory.class);
    private static List<InvocationInvokerOperation> INVOCATION_INVOKER_OPERATIONS = new LinkedList<InvocationInvokerOperation>();
    private static boolean isInit = false;
    private static ServiceInvocationOperation serviceInvocationOperation;

    public static void init() {
        if(!isInit) {
            // TODO register service invocation operation
            registerProcessOperation(new ClusterInvokerOperation());
            registerProcessOperation(new GatewayInvokerOperation());
            registerProcessOperation(new ContextPrepareInvokerOperation());
            registerProcessOperation(new RemoteCallInvokerOperation());
            serviceInvocationOperation = createServiceInvocationOperation();
            isInit = true;
        }
    }

    public static ServiceInvocationOperation getServiceInvocationOperation() {
        return InvokerProcessHandlerFactory.serviceInvocationOperation;
    }

    private static ServiceInvocationOperation createServiceInvocationOperation() {
        if(CollectionUtils.isEmpty(INVOCATION_INVOKER_OPERATIONS)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("InvokerProcessHandlerFactory:createServiceInvocationHandler no operation");
                return null;
            }
        }
        Iterator<InvocationInvokerOperation> iterator = INVOCATION_INVOKER_OPERATIONS.iterator();
        InvocationInvokerOperation serviceInvocationOperation = iterator.next();
        InvocationInvokerOperation invocationInvokerOperation = serviceInvocationOperation;
        InvocationInvokerOperation tmp = null;
        while (iterator.hasNext()) {
            tmp = iterator.next();
            invocationInvokerOperation.setServiceInvocationOperation(tmp);
            invocationInvokerOperation = tmp;
        }
        return serviceInvocationOperation;
    }

    private static void registerProcessOperation(InvocationInvokerOperation invocationInvokerOperation) {
        INVOCATION_INVOKER_OPERATIONS.add(invocationInvokerOperation);
    }

    private static void clearProcessFilter() {
        INVOCATION_INVOKER_OPERATIONS.clear();
    }
}

