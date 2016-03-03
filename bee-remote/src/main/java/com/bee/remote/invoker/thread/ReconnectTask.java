package com.bee.remote.invoker.thread;

import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.domain.ConnectInfo;
import com.bee.remote.invoker.listener.ClusterListener;
import com.bee.remote.invoker.listener.ClusterListenerManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class ReconnectTask implements Runnable, ClusterListener {

    private static final Logger LOGGER = Logger.getLogger(ReconnectTask.class);
    /**
     * 已经关闭的client，等待reconnect
     */
    private final ConcurrentMap<String, Client> CLOSED_CLIENTS = new ConcurrentHashMap<String, Client>();
    /**
     * 工作中的client
     */
    private Map<String, List<Client>> WORKING_CLIENTS = new ConcurrentHashMap<String, List<Client>>();


    public ReconnectTask(Map<String, List<Client>> workingClients) {
        this.WORKING_CLIENTS = workingClients;
    }

    /**
     * 处理连接失败的client
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for(String providerUrl : CLOSED_CLIENTS.keySet()) {
                    Client client = CLOSED_CLIENTS.get(providerUrl);
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[reconnect] checking service provider:" + client);
                    }
                    if(!client.isConnected()) {
                        try {
                            client.connect();
                        } catch (Exception e) {
                            LOGGER.info("[reconnect] connect server[" + providerUrl + "] failed:",  e);
                        }
                    }
                    if(client.isConnected()) {
                        ClusterListenerManager.getInstance().addConnect(client.getConnectInfo());
                        CLOSED_CLIENTS.remove(providerUrl);
                    }
                }
            } catch (Exception e) {
                LOGGER.info("[reconnect] task failed:", e);
            }
        }
    }

    @Override
    public void addConnected(ConnectInfo connectInfo) {
        // nothing to do
    }

    @Override
    public void removeConnected(Client client) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("[reconnect] add service provider to reconnect listener:" + client);
        }
        CLOSED_CLIENTS.putIfAbsent(client.getConnectInfo().getConnect(), client);
    }
}
