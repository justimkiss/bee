package com.bee.remote.invoker.listener;

import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.domain.ConnectInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class ClusterListenerManager {

    private static final Logger LOGGER = Logger.getLogger(ClusterListenerManager.class);

    private List<ClusterListener> CLUSTER_LISTENER = Lists.newArrayList();
    private ConcurrentHashMap<String, ConnectInfo> CONNECT_MAP = new ConcurrentHashMap<String, ConnectInfo>();

    private static ClusterListenerManager clusterListenerManager = new ClusterListenerManager();

    private ClusterListenerManager() {
    }

    public static ClusterListenerManager getInstance() {
        return clusterListenerManager;
    }

    public void addConnect(ConnectInfo connectInfo) {
        ConnectInfo cacheConnectInfo = CONNECT_MAP.get(connectInfo.getConnect());
        if(cacheConnectInfo == null) {
            ConnectInfo oldConnectInfo = CONNECT_MAP.putIfAbsent(connectInfo.getConnect(), connectInfo);
            if(oldConnectInfo != null) {
                cacheConnectInfo = oldConnectInfo;
            }
        }
        if(cacheConnectInfo != null) {
            if(MapUtils.isEmpty(connectInfo.getServiceNames())) {
                if(LOGGER.isInfoEnabled()) {
                    LOGGER.info("[cluster-listener-mgr] add services from:" + connectInfo);
                }
                connectInfo.addServiceNames(cacheConnectInfo.getServiceNames());
            } else {
                cacheConnectInfo.addServiceNames(connectInfo.getServiceNames());
            }
        }
        for(ClusterListener clusterListener : CLUSTER_LISTENER) {
            clusterListener.addConnected(connectInfo);
        }
    }

    public void removeConnect(Client client) {
        ConnectInfo cacheConnectInfo = CONNECT_MAP.get(client.getConnectInfo().getConnect());
        if(cacheConnectInfo != null) {
            for(ClusterListener clusterListener : CLUSTER_LISTENER) {
                clusterListener.removeConnected(client);
            }
        }
    }

    public void registerListener(ClusterListener clusterListener) {
        CLUSTER_LISTENER.add(clusterListener);
    }

    public void cancelListener(ClusterListener clusterListener) {
        CLUSTER_LISTENER.remove(clusterListener);
    }
}
