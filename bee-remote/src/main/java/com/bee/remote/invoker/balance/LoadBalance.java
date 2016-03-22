package com.bee.remote.invoker.balance;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.config.InvokerConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by jeoy.zhou on 3/23/16.
 */
public interface LoadBalance {

    public Client select(List<Client> clients, InvokerConfig<?> invokerConfig, InvocationRequest invocationRequest, Map<String, Integer> weightMap);

}
