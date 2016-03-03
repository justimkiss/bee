package com.bee.remote.common.process.filter;

import com.bee.remote.provider.domain.ProviderContext;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public abstract class AbstractProviderOperation implements ServiceInvocationOperation<ProviderContext> {

    protected ServiceInvocationOperation<ProviderContext> providerOperation;

    public ServiceInvocationOperation<ProviderContext> getProviderOperation() {
        return providerOperation;
    }

    public void setProviderOperation(ServiceInvocationOperation<ProviderContext> providerOperation) {
        this.providerOperation = providerOperation;
    }

    protected boolean hasNext() {
        return this.providerOperation != null;
    }
}
