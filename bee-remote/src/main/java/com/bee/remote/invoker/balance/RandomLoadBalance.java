package com.bee.remote.invoker.balance;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.config.InvokerConfig;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by jeoy.zhou on 3/23/16.
 */
public class RandomLoadBalance implements LoadBalance {

    private static final Logger LOGGER = Logger.getLogger(RandomLoadBalance.class);
    private Random random = new Random();

    @Override
    public Client select(List<Client> clients, InvokerConfig<?> invokerConfig, InvocationRequest invocationRequest, Map<String, Integer> weightMap) {
        int totalWeight = 0;
        boolean weightAllSame = true;
        int weight = weightMap.get(clients.get(0).getAddress());
        for (Client client : clients) {
            Integer tmp = weightMap.get(client.getAddress());
            tmp = (tmp == null) ? Constants.DEFAULT_WEIGHT : tmp;
            totalWeight += tmp;
            if (weightAllSame && weight != tmp) {
                weightAllSame = false;
            }
            weight = tmp;
        }
        if (weightAllSame)
            return clients.get(random.nextInt(clients.size()));
        int weightPoint = random.nextInt(totalWeight);
        for (Client client : clients) {
            Integer tmp = weightMap.get(client.getAddress());
            tmp = (tmp == null) ? Constants.DEFAULT_WEIGHT : tmp;
            weightPoint -= tmp;
            if (weightPoint < 0) {
                return client;
            }
        }
        return null;
    }
}
