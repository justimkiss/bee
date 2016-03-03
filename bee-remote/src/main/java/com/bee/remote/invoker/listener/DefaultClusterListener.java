package com.bee.remote.invoker.listener;

import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.ClientSelector;
import com.bee.remote.invoker.domain.ConnectInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jeoy.zhou on 2/16/16.
 */
public class DefaultClusterListener implements ClusterListener{

    private static final Logger LOGGER = Logger.getLogger(DefaultClusterListener.class);

    private Map<String, List<Client>> workingClients;
    private Map<String, Client> allClients;

    public DefaultClusterListener(Map<String, List<Client>> workingClients, Map<String, Client> allClients) {
        this.workingClients = workingClients;
        this.allClients = allClients;
    }


    @Override
    public void addConnected(ConnectInfo connectInfo) {
        if(LOGGER.isInfoEnabled())
            LOGGER.info("[cluster-listener] add service provider:" + connectInfo);
        Client client = allClients.get(connectInfo.getConnect());
        if(clientExists(connectInfo)) {
            if(client == null) return;
        }
        if (client == null) {
            client = ClientSelector.createClient(connectInfo);
            Client oldClient = allClients.putIfAbsent(connectInfo.getConnect(), client);
            if(oldClient != null)
                client = oldClient;
        }
        try {
            if (!client.isConnected()) {
                client.connect();
            } else {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("client already connected:" + client);
            }
            if (client.isConnected()) {
                for(String serviceUrl : connectInfo.getServiceNames().keySet()) {
                    List<Client> clientList = workingClients.get(serviceUrl);
                    if(clientList == null) {
                        clientList = new ArrayList<Client>();
                        List<Client> oldClientList = workingClients.putIfAbsent(serviceUrl, clientList);
                        if(oldClientList != null) {
                            clientList = oldClientList;
                        }
                    }
                    if (!clientList.contains(client))
                        clientList.add(client);
                }
            } else {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("[cluster-listener] remove client:" + client);
                ClusterListenerManager.getInstance().removeConnect(client);
            }
        } catch (Exception e) {
            LOGGER.error("DefaultClusterListener: addConnect error", e);
        }


    }

    @Override
    public void removeConnected(Client client) {
        if(LOGGER.isInfoEnabled())
            LOGGER.info("[cluster-listener] remove service provider:" + client);
        for(List<Client> clients : workingClients.values()) {
            if(CollectionUtils.isNotEmpty(clients))
                clients.remove(client);
        }
    }

    private boolean clientExists(ConnectInfo connectInfo) {
        for(String serviceUrl : connectInfo.getServiceNames().keySet()) {
            List<Client> clientList = workingClients.get(serviceUrl);
            if(CollectionUtils.isNotEmpty(clientList)) {
                for(Client client : clientList) {
                    if(client.getAddress().equals(connectInfo.getConnect()))
                        return true;
                }
            }
        }
        return false;
    }
}
