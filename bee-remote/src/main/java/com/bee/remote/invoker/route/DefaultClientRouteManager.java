package com.bee.remote.invoker.route;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.ClientManager;
import com.bee.remote.invoker.balance.LoadBalance;
import com.bee.remote.invoker.balance.RandomLoadBalance;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.exception.ServiceUnavailableException;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public class DefaultClientRouteManager implements ClientRouteManager{

    private static final Logger LOGGER = Logger.getLogger(DefaultClientRouteManager.class);
    private LoadBalance loadBalance = new RandomLoadBalance();

    @Override
    public Client getClient(List<Client> clients, InvokerConfig<?> invokerConfig, InvocationRequest invocationRequest) throws ServiceUnavailableException{
        List<Client> clientList = Lists.newArrayList(clients);
        Client client = null;
        while ((client = selectBestClient(clientList, invokerConfig, invocationRequest)) != null) {
            if (client.isConnected()) break;
            client.connect();
            if (client.isConnected()) break;
            clientList.remove(client);
            if (CollectionUtils.isEmpty(clientList)) break;
        }
        if (!client.isConnected())
            throw new ServiceUnavailableException("no available server exists for service[" + invokerConfig + "]");
        return client;
    }

    /**
     * 择优选取客户端
     * @param clients
     * @param invokerConfig
     * @param invocationRequest
     * @return
     */
    private Client selectBestClient(List<Client> clients, InvokerConfig<?> invokerConfig, InvocationRequest invocationRequest) {
        assert(CollectionUtils.isNotEmpty(clients) && clients.size() >= 1);
        Client result = null;
        if (clients.size() == 1) {
            result = clients.get(0);
            assert(result.isActive());
            return result;
        }
        return loadBalance.select(clients, invokerConfig, invocationRequest, ClientManager.getInstance().getWeightFromCaches(clients));
//        long minCapacity = Long.MAX_VALUE;
//        Map<String, Integer> weightCache = ClientManager.getInstance().getWeightFromCaches(clients);
//        for (Client client : clients) {
//            if (!client.isActive()) continue;
//            Integer weight = weightCache.get(client.getAddress());
//            weight = weight == null ? Constants.DEFAULT_WEIGHT : weight;
//            long tmp = weight * InvokerStaticsHolder.getCapacityValue(client.getAddress());
//            if (tmp < minCapacity) {
//                minCapacity = tmp;
//                result = client;
//            }
//        }
//        return result;
    }

}
