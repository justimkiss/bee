package com.bee.remote.invoker.thread;

import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.listener.ClusterListenerManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class HeartBeatTask implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(HeartBeatTask.class);
    private final Map<String, List<Client>> WORKING_CLIENTS;

    public HeartBeatTask(Map<String, List<Client>> workingClients) {
        this.WORKING_CLIENTS = workingClients;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // TODO 考虑本地service重复
                if(WORKING_CLIENTS != null) {
                    Set<Client> clients = new HashSet<Client>();
                    for(List<Client> tmpClients : WORKING_CLIENTS.values()) {
                        if(CollectionUtils.isNotEmpty(tmpClients))
                            clients.addAll(tmpClients);
                    }
                    for(Client client : clients) {
                        if(LOGGER.isDebugEnabled()) {
                            LOGGER.debug("[heartbeat] checking service provider:" + client);
                        }
                        if(client.isConnected()) {
                            sendHeartBeatRequest(client);
                        } else {
                            if(LOGGER.isInfoEnabled()) {
                                LOGGER.info("[heartbeat] remove connect:" + client);
                            }
                            ClusterListenerManager.getInstance().removeConnect(client);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("[heartbeat] task failed:", e);
            }
        }
    }

    private void sendHeartBeatRequest(Client client) {
        // TODO 处理心跳请求
    }

}
