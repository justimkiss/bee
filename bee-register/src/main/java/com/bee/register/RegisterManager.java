package com.bee.register;

import com.bee.common.constants.Constants;
import com.bee.common.domain.HostInfo;
import com.bee.common.extension.ExtensionLoader;
import com.bee.register.config.DefaultRegisterConfigManager;
import com.bee.register.config.RegisterConfigManager;
import com.jeoy.bee.monitor.Monitor;
import com.jeoy.bee.monitor.MonitorManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/18/15.
 */
public class RegisterManager {

    private static final Logger LOGGER = Logger.getLogger(RegisterManager.class);
    private static RegisterManager registerManager = new RegisterManager();
    private static final Register REGISTER = ExtensionLoader.getExtension(Register.class);
    private static final RegisterConfigManager REGISTER_CONFIG_MANAGER = new DefaultRegisterConfigManager();
    private static final Monitor MONITOR = MonitorManager.getMonitor();
    /**
     * key: serviceurl
     * value: set<hostInfo>
     */
    private static final ConcurrentHashMap<String, Set<HostInfo>> SERVICE_ADDRESS_CACHE = new ConcurrentHashMap<String, Set<HostInfo>>();
    /**
     * key: host:port
     * value: hostInfo
     */
    private static final ConcurrentHashMap<String, HostInfo> ALL_REFERENCED_CACHE = new ConcurrentHashMap<String, HostInfo>();
    private static Properties properties;

    private static boolean isInit = false;

    private RegisterManager(){}

    public static RegisterManager getInstance() {
        if(!isInit) {
            synchronized (RegisterManager.class) {
                if(!isInit) {
                    init(REGISTER_CONFIG_MANAGER.getRegisterConfig());
                    isInit = true;
                }
            }
        }
        return registerManager;
    }

    private static void init(Properties properties) {
        registerManager.setProperties(properties);
        REGISTER.init(properties);
    }

    public Register getRegister() {
        return REGISTER;
    }


    public int getServiceWeight(String serviceName, String serverAddress) {
        return REGISTER.getServiceWeight(serviceName, serverAddress);
    }

    /**
     * 获取serviceAddress列表字符串，以,隔开
     * @param serviceName
     * @return
     */
    public String getServiceAddress(String serviceName) {
        List<String> serviceAddressList = getServiceAddressList(serviceName);
        if(CollectionUtils.isEmpty(serviceAddressList)) return null;
        return StringUtils.join(serviceAddressList, Constants.DOT_SEPARATOR);
    }

    /**
     * 获取serviceAddress列表
     * @param serviceName
     * @return
     */
    public List<String> getServiceAddressList(String serviceName) {
        if(REGISTER == null || StringUtils.isBlank(serviceName)) return null;
        return REGISTER.getServiceAddressList(serviceName);
    }

    /**
     * 注册服务
     * @param serviceName
     * @param serverAddress
     * @param weight
     */
    public void registerService(String serviceName, String serverAddress, int weight) {
        REGISTER.registerService(serviceName, serverAddress, weight);
        MONITOR.logEvent("BeeService.register", serviceName, "serverAddress: " + serverAddress + ", weight: " + weight);
    }

    /**
     * 注销服务
     * @param serviceName
     * @param serverAddress
     */
    public void unregisterService(String serviceName, String serverAddress) {
        REGISTER.unregisterService(serviceName, serverAddress);
        MONITOR.logEvent("BeeService.unregister", serviceName, "serverAddress: " + serverAddress);
    }

    /**
     * 获取缓存的服务提供者的host信息
     * @param serviceName
     * @return
     */
    public Set<HostInfo> getCacheServiceHostInfoByServiceName(String serviceName) {
        return SERVICE_ADDRESS_CACHE.get(serviceName);
    }

    public HostInfo getCacheServiceHostInfoByAddress(String address) {
        return ALL_REFERENCED_CACHE.get(address);
    }

    /**
     * 缓存服务信息
     * @param hostInfo
     */
    public void cacheServiceHostInfo(HostInfo hostInfo) {
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
    public void removeCacheServiceHostInfo(HostInfo hostInfo) {
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

    public void setServiceWeight(String serviceAddress, int weight) {
        HostInfo hostInfo = ALL_REFERENCED_CACHE.get(serviceAddress);
        hostInfo.setWeight(weight);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("set " + serviceAddress + " weight to " + weight);
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

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        RegisterManager.properties = properties;
    }
}
