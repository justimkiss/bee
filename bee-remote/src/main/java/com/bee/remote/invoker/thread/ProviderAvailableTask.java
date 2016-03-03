package com.bee.remote.invoker.thread;

import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.ClientManager;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.spring.ServiceFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class ProviderAvailableTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ProviderAvailableTask.class);
    private Map<String, List<Client>> WORKING_CLIENT;


    public ProviderAvailableTask(Map<String, List<Client>> workingClients) {
        this.WORKING_CLIENT = workingClients;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Set<InvokerConfig<?>> invokerConfigs = ServiceFactory.getAllInvokersServices().keySet();
                for(InvokerConfig<?> invokerConfig : invokerConfigs) {
                    String url = invokerConfig.getUrl();
                    int available = availableClients(WORKING_CLIENT.get(url));
                    if(available > 0) {
                        try {
                            ClientManager.getInstance().registerClient(url);
                        } catch (Exception e) {
                            LOGGER.error("[provider-available] registerClient error:", e);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("[provider-available] task failed:", e);
            }
        }
    }

    private int availableClients(List<Client> clients) {
        if(CollectionUtils.isEmpty(clients)) {
            return 0;
        }
        int available = 0;
        for(Client client : clients) {
            if(client.isConnected())
                available += 1;
        }
        return available;
    }
}
