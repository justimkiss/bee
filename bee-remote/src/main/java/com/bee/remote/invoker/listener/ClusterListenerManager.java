package com.bee.remote.invoker.listener;

import com.bee.common.constants.Constants;
import com.bee.register.listen.RegisterListenManager;
import com.bee.register.listen.event.ProviderChangeEnum;
import com.bee.register.listen.event.ServiceProviderChangeEvent;
import com.bee.register.listen.listener.ServiceProviderChangeListener;
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
        RegisterListenManager.addListener(new InnerServiceProviderChangeListener());
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

    class InnerServiceProviderChangeListener implements ServiceProviderChangeListener {
        @Override
        public void providerChange(ServiceProviderChangeEvent event) {
            if (event.getProviderChangeEnum() == ProviderChangeEnum.REMOVE) {
                String address = event.getHost() + Constants.COLON_SYMBOL + event.getPort();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("[cluster-listener-mgr] remove:" + address + " from " + event.getServiceName());
                ConnectInfo connectInfo = CONNECT_MAP.get(event.getServiceName());
                if (connectInfo != null) {
                    connectInfo.getServiceNames().remove(event.getServiceName());
                    if (connectInfo.getServiceNames().size() == 0)
                        CONNECT_MAP.remove(address);
                }
                for (ClusterListener listener : CLUSTER_LISTENER) {
                    listener.removeService(event.getServiceName(), event.getHost(), event.getPort());
                }
            }
        }

        @Override
        public void weightChange(ServiceProviderChangeEvent event) {
            // nothind to do
        }
    }
}
