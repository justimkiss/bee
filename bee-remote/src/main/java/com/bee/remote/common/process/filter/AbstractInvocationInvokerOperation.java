package com.bee.remote.common.process.filter;

import com.bee.remote.invoker.domain.InvokerContext;

/**
 * Created by jeoy.zhou on 1/31/16.
 */
public abstract class AbstractInvocationInvokerOperation implements ServiceInvocationOperation<InvokerContext>{

    protected ServiceInvocationOperation<InvokerContext>  serviceInvocationOperation;

    public void setServiceInvocationOperation(ServiceInvocationOperation<InvokerContext> serviceInvocationOperation) {
        this.serviceInvocationOperation = serviceInvocationOperation;
    }

    protected ServiceInvocationOperation<InvokerContext> getServiceInvocationOperation() {
        return this.serviceInvocationOperation;
    }

    protected boolean hasNext() {
        return this.serviceInvocationOperation != null;
    }
}
