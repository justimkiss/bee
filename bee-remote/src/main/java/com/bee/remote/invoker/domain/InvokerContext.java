package com.bee.remote.invoker.domain;

import com.bee.remote.common.codec.domain.InvocationContext;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.config.InvokerConfig;

/**
 * Created by jeoy.zhou on 1/31/16.
 */
public interface InvokerContext extends InvocationContext{

    public InvokerConfig<?> getInvokerConfig();

    public String getMethodName();

    public Class<?>[] getParameterTypes();

    public Object[] getArguments();

    public Client getClient();

    public void setClient(Client client);

    public String getMessageType();
}
