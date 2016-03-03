package com.bee.remote.provider;

import com.bee.common.constants.Constants;
import com.bee.common.extension.ExtensionLoader;
import com.bee.config.ConfigManager;
import com.bee.config.loader.ConfigManagerLoader;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.config.ServiceConfig;
import com.bee.remote.provider.listener.ShutdownHookListener;
import com.bee.remote.provider.server.Server;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public final class ProviderBootStrap {

    private static final Logger LOGGER = Logger.getLogger(ProviderBootStrap.class);
    private static final Map<String, Server> SERVICE_MAP = new HashMap<String, Server>();
    private static Server HTTP_SERVER;
    private static volatile boolean isInit = false;
    private static Date startDate;

    public static void init() {
        if(!isInit) {
            ConfigManager configManager = ConfigManagerLoader.getConfigManager();
            Thread shutdownHook = new Thread(new ShutdownHookListener());
            shutdownHook.setDaemon(true);
            shutdownHook.setPriority(Thread.MAX_PRIORITY);
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            // TODO 初始化http

            isInit = true;
        }
    }

    public static void start(ProviderConfig<?> providerConfig) {
        ServiceConfig serviceConfig = providerConfig.getServiceConfig();
        if(serviceConfig == null) {
            throw new IllegalArgumentException("serviceConfig is required");
        }
        String key = serviceConfig.getProtocol() + serviceConfig.getPort();
        Server server = null;
        if((server = SERVICE_MAP.get(key)) != null) {
            server.addService(providerConfig);
        } else {
            synchronized (ProviderBootStrap.class) {
                List<Server> servers = ExtensionLoader.newExtensionList(Server.class);
                for(Server tempServer : servers) {
                    if(tempServer != null
                            && tempServer.support(providerConfig.getServiceConfig())
                            && !tempServer.isStart()) {
                        tempServer.start(serviceConfig);
                        tempServer.addService(providerConfig);
                        SERVICE_MAP.put(tempServer.Protocol() + serviceConfig.getPort(), tempServer);
                        LOGGER.info(String.format("Bee server: %s has been started", tempServer));
                        break;
                    }
                }
            }
        }
    }

    public static List<Server> getServers(ProviderConfig<?> providerConfig) {
        List<Server> result = new ArrayList<Server>();
        if (HTTP_SERVER != null) {
            result.add(HTTP_SERVER);
        }
        ServiceConfig serviceConfig = providerConfig.getServiceConfig();
        Server server = SERVICE_MAP.get(serviceConfig.getProtocol() +
                Constants.COLON_SYMBOL + serviceConfig.getPort());
        if (server != null) {
            result.add(server);
        }
        return result;
    }

    public static Map<String, Server> getServiceMap() {
        return SERVICE_MAP;
    }

    public static void shutdown() {
        synchronized (ProviderBootStrap.class) {
            for(Server server : SERVICE_MAP.values()) {
                if(server != null) {
                    LOGGER.info("start to stop: " + server);
                    try {
                        server.shutdown();
                    } catch (Exception e) {
                        LOGGER.error(String.format("stop service[%d] occur error", server), e);
                    }
                    LOGGER.info(server + "has been shutdown");
                }
            }
        }
    }
}
