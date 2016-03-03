package com.bee.remote.provider.processor;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.domain.Disposable;
import com.bee.remote.provider.domain.ProviderContext;

import java.util.concurrent.Future;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public interface RequestProcessor extends Disposable {

    public void start();

    public boolean needCancelRequest(InvocationRequest request);

    public String getProcessorStatus(InvocationRequest request);

    public Future<InvocationResponse> processRequest(InvocationRequest request, ProviderContext providerContext);
}
