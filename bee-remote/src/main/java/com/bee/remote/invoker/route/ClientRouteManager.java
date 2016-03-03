package com.bee.remote.invoker.route;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.config.InvokerConfig;

import java.util.List;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public interface ClientRouteManager {

    public Client getClient(List<Client> clients, InvokerConfig<?> invokerConfig, InvocationRequest invocationRequest);

}
