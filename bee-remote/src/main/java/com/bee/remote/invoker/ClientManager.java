package com.bee.remote.invoker;

import com.bee.common.constants.Constants;
import com.bee.common.domain.HostInfo;
import com.bee.register.RegisterManager;
import com.bee.register.listen.RegisterListenManager;
import com.bee.register.listen.event.ProviderChangeEnum;
import com.bee.register.listen.event.ServiceProviderChangeEvent;
import com.bee.register.listen.listener.ServiceProviderChangeListener;
import com.bee.remote.common.codec.domain.InvocationContext;
import com.bee.remote.common.domain.Disposable;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.ConnectInfo;
import com.bee.remote.invoker.exception.ServiceUnavailableException;
import com.bee.remote.invoker.listener.ClusterListenerManager;
import com.bee.remote.invoker.listener.DefaultClusterListener;
import com.bee.remote.invoker.route.ClientRouteManager;
import com.bee.remote.invoker.route.DefaultClientRouteManager;
import com.bee.remote.invoker.thread.HeartBeatTask;
import com.bee.remote.invoker.thread.ProviderAvailableTask;
import com.bee.remote.invoker.thread.ReconnectTask;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class ClientManager implements Disposable{

    private static final Logger LOGGER = Logger.getLogger(ClientManager.class);
    /**
     * key: serviceurl
     * value: set<hostInfo>
     */
    private static final Map<String, Set<HostInfo>> SERVICE_ADDRESS_CACHE = new ConcurrentHashMap<String, Set<HostInfo>>();
    /**
     * key: host:port
     * value: hostInfo
     */
    private static final Map<String, HostInfo> ALL_REFERENCED_CACHE = new ConcurrentHashMap<String, HostInfo>();

    /**
     * 缓存client
     * key: serviceUrl
     * values: 所有对应service的address客户端
     */
    private static final ConcurrentHashMap<String, List<Client>> SERVICE_CLIENTS = new ConcurrentHashMap<String, List<Client>>();
    /**
     * 所有client
     * key: service的address
     * value: address客户端
      */
    private static final ConcurrentHashMap<String, Client> ALL_SERVICE_CLIENTS = new ConcurrentHashMap<String, Client>();
    private static final ClientRouteManager CLIENT_ROUTE_MANAGER = new DefaultClientRouteManager();
    private final ServiceProviderChangeListener SERVICE_PROVIDER_CHANGE_LISTENER = new ClientManagerServiceProviderChangeListener();
    private static final ScheduledThreadPoolExecutor HEART_BEAT_THREAD_POOL = new ScheduledThreadPoolExecutor(1);
    private static final ScheduledThreadPoolExecutor RECONNECT_THREAD_POOL = new ScheduledThreadPoolExecutor(1);
    private static final ScheduledThreadPoolExecutor PROVIDER_AVAILABLE_THREAD_POOL = new ScheduledThreadPoolExecutor(1);
    private static final ClientManager CLIENT_MANAGER = new ClientManager();
    private DefaultClusterListener defaultClusterListener;

    private static final long HEART_BEAT_INTERVAL = 1l;
    private static final long RECONNECT_INTERVAL = 1l;
    private static final long PROVIDER_INTERVAL = 1l;

    private ClientManager() {
        HeartBeatTask heartBeatTask = new HeartBeatTask(SERVICE_CLIENTS);
        ReconnectTask reconnectTask = new ReconnectTask(SERVICE_CLIENTS);
        ProviderAvailableTask providerAvailableTask = new ProviderAvailableTask(SERVICE_CLIENTS);
        //TODO 去除注释
//        HEART_BEAT_THREAD_POOL.scheduleAtFixedRate(heartBeatTask, HEART_BEAT_INTERVAL, HEART_BEAT_INTERVAL, TimeUnit.SECONDS);
//        RECONNECT_THREAD_POOL.scheduleAtFixedRate(reconnectTask, RECONNECT_INTERVAL, RECONNECT_INTERVAL, TimeUnit.SECONDS);
//        PROVIDER_AVAILABLE_THREAD_POOL.scheduleAtFixedRate(providerAvailableTask, PROVIDER_INTERVAL, PROVIDER_INTERVAL, TimeUnit.SECONDS);

        defaultClusterListener = new DefaultClusterListener(SERVICE_CLIENTS, ALL_SERVICE_CLIENTS);
        ClusterListenerManager.getInstance().registerListener(defaultClusterListener);
        ClusterListenerManager.getInstance().registerListener(reconnectTask);
        RegisterListenManager.addListener(SERVICE_PROVIDER_CHANGE_LISTENER);
    }

    public static  ClientManager getInstance() {
        return CLIENT_MANAGER;
    }

    /**
     * 注册调用客户端信息
     * @param serviceName
     */
    public void registerClient(String serviceName) {
        List<String> serviceAddressList = getServiceAddress(serviceName);
        String[] strArr = null;
        String ip = null;
        int port = 0;
        for(String addressStr : serviceAddressList) {
            if(StringUtils.isBlank(addressStr)) continue;
            strArr = addressStr.split(Constants.COLON_SYMBOL);
            if(strArr.length != 2) continue;
            try {
                ip = strArr[0];
                port = Integer.valueOf(strArr[1]);
            } catch (RuntimeException e) {
                LOGGER.warn("ClientManager: registerClient invalid addressString: " + addressStr);
                continue;
            }
            if(StringUtils.isBlank(ip) || port < 0) continue;
            int weight = RegisterManager.getInstance().getServiceWeight(serviceName, addressStr);
            RegisterListenManager.providerChanged(serviceName, ip, port, weight, ProviderChangeEnum.ADD);
        }
    }

    public Client getClient(InvokerConfig<?> invokerConfig, InvocationContext invocationContext) throws ServiceUnavailableException{
        List<Client> clients = getClientList(invokerConfig.getUrl());
        if (LOGGER.isDebugEnabled())
            for (Client client : clients)
                LOGGER.debug("available service provider：\t" + client.getAddress());
        return CLIENT_ROUTE_MANAGER.getClient(clients, invokerConfig, invocationContext.getRequest());
    }

    /**
     * 获取客户端信息
     * @param serviceUrl
     * @return
     */
    public List<Client> getClientList(String serviceUrl) {
        List<Client> clientList = SERVICE_CLIENTS.get(serviceUrl);
        if (CollectionUtils.isEmpty(clientList))
            throw new ServiceUnavailableException("no available provider for service:" + serviceUrl);
        return clientList;
    }


    /**
     * 获取指定服务权重
     * @param clients
     * @return
     */
    public Map<String, Integer> getWeightFromCaches(List<Client> clients) {
        if (CollectionUtils.isEmpty(clients)) return null;
        List<String> addresses = Lists.newArrayListWithCapacity(clients.size());
        for (Client client : clients) {
            addresses.add(client.getAddress());
        }
        return getWeightFromCache(addresses);
    }

    /**
     * 获取指定服务权重
     * @param addresses
     * @return
     */
    public Map<String, Integer> getWeightFromCache(List<String> addresses) {
        if (CollectionUtils.isEmpty(addresses))
            return null;
        Map<String, Integer> result = Maps.newHashMapWithExpectedSize(addresses.size());
        HostInfo hostInfo = null;
        for (String address : addresses) {
            hostInfo = ALL_REFERENCED_CACHE.get(address);
            if (hostInfo == null) continue;
            result.put(address, hostInfo.getWeight());
        }
        return result;
    }

    /**
     * 获取服务信息
     * @param serviceName
     * @return
     */
    private List<String> getServiceAddress(String serviceName) {
        if(StringUtils.isBlank(serviceName))
            throw new ServiceUnavailableException("ClientManager: getServiceAddress param[serviceName] is null");
        List<String> serviceAddressList= RegisterManager.getInstance().getServiceAddressList(serviceName);
        if(CollectionUtils.isEmpty(serviceAddressList))
            throw new ServiceUnavailableException("ClientManager: not getServiceAddress by serviceName: " + serviceName);
        if(LOGGER.isInfoEnabled())
            LOGGER.info("ClientManager: getServiceAddress " + serviceAddressList.toString() + " by serviceName: " + serviceName);
        return serviceAddressList;
    }

    /**
     * 缓存服务信息
     * @param hostInfo
     */
    private void cacheServiceHostInfo(HostInfo hostInfo) {
        if(!validHostInfo(hostInfo)) {
            LOGGER.error(String.format("ClientManager: cacheServiceHostInfo param[%s] is invalid", hostInfo));
            return;
        }
        Set<HostInfo> set = SERVICE_ADDRESS_CACHE.get(hostInfo.getServiceName());
        if(set == null) {
            set = new HashSet<HostInfo>();
            Set<HostInfo> oldData = SERVICE_ADDRESS_CACHE.putIfAbsent(hostInfo.getServiceName(), set);
            if(oldData != null) {
                set = oldData;
            }
        }
        set.add(hostInfo);
        ALL_REFERENCED_CACHE.put(hostInfo.getConnect(), hostInfo);
    }

    /**
     * 清楚缓存的服务信息
     * @param hostInfo
     */
    private void removeCacheServiceHostInfo(HostInfo hostInfo) {
        if(!simpleValidHostInfo(hostInfo)) {
            LOGGER.error(String.format("ClientManager: removeCacheServiceHostInfo param[%s] is invalid", hostInfo));
            return;
        }
        Set<HostInfo> cache = SERVICE_ADDRESS_CACHE.get(hostInfo.getServiceName());
        if(cache == null) {
            LOGGER.warn("ClientManager: removeCacheServiceHostInfo can not find by serviceName: " + hostInfo.getServiceName());
            return;
        }
        cache.remove(hostInfo);
        ALL_REFERENCED_CACHE.remove(hostInfo.getConnect());
    }


    /**
     * 简单验证hostInfo合法性
     * @param hostInfo
     * @return
     */
    private boolean simpleValidHostInfo(HostInfo hostInfo) {
        return hostInfo != null
                && StringUtils.isNotBlank(hostInfo.getHost())
                && StringUtils.isNotBlank(hostInfo.getServiceName())
                && hostInfo.getPort() > 0;
    }

    /**
     * 验证hostinfo合法性
     * @param hostInfo
     * @return
     */
    private boolean validHostInfo(HostInfo hostInfo) {
        return hostInfo != null
                && StringUtils.isNotBlank(hostInfo.getHost())
                && hostInfo.getPort() > 1
                && hostInfo.getWeight() >= Constants.MIN_WEIGHT
                && hostInfo.getWeight() <= Constants.MAX_WEIGHT
                && StringUtils.isNotBlank(hostInfo.getServiceName());
    }


    class ClientManagerServiceProviderChangeListener implements ServiceProviderChangeListener {
        @Override
        public void providerChange(ServiceProviderChangeEvent event) {
            if(event == null || event.getProviderChangeEnum() == null) return;
            HostInfo hostInfo = new HostInfo(event.getServiceName(), event.getHost(), event.getPort(), event.getWeight());
            if(event.getProviderChangeEnum().equals(ProviderChangeEnum.ADD)) {
                ConnectInfo connectInfo = new ConnectInfo(event.getServiceName(), event.getHost(), event.getPort(), event.getWeight());
                ClusterListenerManager.getInstance().addConnect(connectInfo);
                // chche service info
                cacheServiceHostInfo(hostInfo);
            } else if (event.getProviderChangeEnum().equals(ProviderChangeEnum.REMOVE)) {
                // remove service info
                removeCacheServiceHostInfo(hostInfo);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        ClusterListenerManager clusterListenerManager = ClusterListenerManager.getInstance();
        if (clusterListenerManager instanceof Disposable) {
            ((Disposable) clusterListenerManager).destroy();
        }
        if (CLIENT_ROUTE_MANAGER instanceof  Disposable) {
            ((Disposable) CLIENT_ROUTE_MANAGER).destroy();
        }
        if (HEART_BEAT_THREAD_POOL != null)
            HEART_BEAT_THREAD_POOL.shutdown();
        if (RECONNECT_THREAD_POOL != null)
            RECONNECT_THREAD_POOL.shutdown();
        if (PROVIDER_AVAILABLE_THREAD_POOL != null)
            PROVIDER_AVAILABLE_THREAD_POOL.shutdown();
        defaultClusterListener.destroy();
    }
}
