package com.bee.remote.invoker.listener;

import com.bee.remote.common.domain.Disposable;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.ClientSelector;
import com.bee.remote.invoker.domain.ConnectInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/16/16.
 */
public class DefaultClusterListener implements ClusterListener, Disposable {

    private static final Logger LOGGER = Logger.getLogger(DefaultClusterListener.class);
    private static final long DEFAULT_WAIT = 3000;
    private ScheduledThreadPoolExecutor closeExecutor;
    private ConcurrentHashMap<String, List<Client>> workingClients;
    private ConcurrentHashMap<String, Client> allClients;

    public DefaultClusterListener(ConcurrentHashMap<String, List<Client>> workingClients, ConcurrentHashMap<String, Client> allClients) {
        this.workingClients = workingClients;
        this.allClients = allClients;
        this.closeExecutor = new ScheduledThreadPoolExecutor(3);
    }


    @Override
    public void addConnected(ConnectInfo connectInfo) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("[cluster-listener] add service provider:" + connectInfo);
        Client client = allClients.get(connectInfo.getConnect());
        if (clientExists(connectInfo)) {
            if (client == null) return;
        }
        if (client == null) {
            client = ClientSelector.createClient(connectInfo);
            Client oldClient = allClients.putIfAbsent(connectInfo.getConnect(), client);
            if (oldClient != null)
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
                for (String serviceUrl : connectInfo.getServiceNames().keySet()) {
                    List<Client> clientList = workingClients.get(serviceUrl);
                    if (clientList == null) {
                        clientList = new ArrayList<Client>();
                        List<Client> oldClientList = workingClients.putIfAbsent(serviceUrl, clientList);
                        if (oldClientList != null) {
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
        if (LOGGER.isInfoEnabled())
            LOGGER.info("[cluster-listener] remove service provider:" + client);
        for (List<Client> clients : workingClients.values()) {
            if (CollectionUtils.isNotEmpty(clients))
                clients.remove(client);
        }
    }

    @Override
    public void removeService(String serviceName, String host, int port) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("[cluster-listener] do removeService provider:" + serviceName + ":" + host + ":" + port);
        List<Client> clients = workingClients.get(serviceName);
        List<Client> newClients = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clients)) {
            newClients.addAll(clients);
        }
        Client client = null;
        for (Client tmp : clients) {
            if (tmp != null && tmp.getHost().equals(host) && tmp.getPort() == port) {
                newClients.remove(tmp);
                client = tmp;
                break;
            }
        }
        workingClients.put(serviceName, newClients);
        if (client != null) {
            allClients.remove(client.getAddress());
            closeClientFuture(client);
        }
    }

    private void closeClientFuture(final Client client) {
        try {
            this.closeExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    client.close();
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("close client:" + client.getAddress());
                }
            }, DEFAULT_WAIT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("error schedule task to close client", e);
        }
    }


    private boolean clientExists(ConnectInfo connectInfo) {
        for (String serviceUrl : connectInfo.getServiceNames().keySet()) {
            List<Client> clientList = workingClients.get(serviceUrl);
            if (CollectionUtils.isNotEmpty(clientList)) {
                for (Client client : clientList) {
                    if (client.getAddress().equals(connectInfo.getConnect()))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void destroy() throws Exception {
        try {
            this.closeExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
