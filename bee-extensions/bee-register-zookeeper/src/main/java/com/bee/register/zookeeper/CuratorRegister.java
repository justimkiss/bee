package com.bee.register.zookeeper;


import com.bee.common.constants.Constants;
import com.bee.common.exception.RegisterException;
import com.bee.register.Register;
import com.bee.register.zookeeper.utils.CuratorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

/**
 * Created by jeoy.zhou on 12/18/15.
 */
public class CuratorRegister implements Register {

    private static final Logger LOGGER = Logger.getLogger(CuratorRegister.class);
    private CuratorClient client;
    private Properties properties;
    private static boolean isInit = false;

    private static final String ZOOKEEPER_CONFIG_TYPE="zookeeper.config.type";
    private static final String ZOOKEEPER_ADDRESS_KEY = "zookeeper.services.url";
    private static final String ZOOKEEPER_SERVICES_KEY = "zookeeper.services.namespace";
    private static final String SESSION_TIMEOUT_KEY = "zookeeper.session.timeout";
    private static final String CONNECTION_TIMEOUT_KEY = "zookeeper.connection.timeout";
    private static final String RETRY_TIME_LIMIT_KEY = "zookeeper.retry.time.limit";
    private static final String RETRY_INTERVAL_KRY = "zookeeper.retry.interval";


    @Override
    public void init(Properties properties) {
        if(!isInit) {
            synchronized (this) {
                if(!isInit) {
                    try {
                        this.properties = properties;
                        createClient();
                    } catch (Exception e) {
                        LOGGER.error("CuratorRegister init error", e);
                        System.exit(0);
                    }
                    isInit = true;
                }
            }
        }
    }

    @Override
    public String name() {
        return "zookeeper";
    }

    /**
     * 获取指定服务的provider
     * @param serviceName
     * @return
     */
    @Override
    public List<String> getServiceAddressList(String serviceName) {
        if(StringUtils.isBlank(serviceName))
            throw new IllegalArgumentException("CuratorRegister: getRegisterAddress param[serviceName] is null");
        try {
            List<String> result = this.client.getChildrenNodes(CuratorUtils.getRegisterServiceProviderRootPath(serviceName), true);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("CuratorRegister: getRegisterAddress serviceName[%s], children node[%s]", serviceName, result));
            }
            return result;
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: getRegisterAddress failed to getRegisterAddress serviceName[%s]", serviceName));
            throw new RegisterException(e);
        }
    }

    /**
     * 获取指定服务的消费者
     * @param serviceName
     * @return
     */
    public List<String> getConsumerAddress(String serviceName) {
        if(StringUtils.isBlank(serviceName))
            throw new IllegalArgumentException("CuratorRegister: getRegisterAddress param[serviceName] is null");
        try {
            List<String> result = this.client.getChildrenNodes(CuratorUtils.getRegisterServiceConsumerRootPath(serviceName), false);
            if(LOGGER.isDebugEnabled())
                LOGGER.debug(String.format("CuratorRegister: getRegisterAddress serviceName[%s], children node[%s]", serviceName, result));
            return result;
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: getRegisterAddress failed to getConsumerAddress serviceName[%s]", serviceName));
            throw new RegisterException(e);
        }
    }

    @Override
    public void registerService(String serviceName, String serverAddress, int weight) throws RegisterException {
        if(StringUtils.isBlank(serviceName))
            throw new IllegalArgumentException("CuratorRegister: registerService param[serviceName] is null");
        if(StringUtils.isBlank(serverAddress))
            throw new IllegalArgumentException("CuratorRegister: registerService param[serverAddress] is null");
        try {
            int serviceWeight = Constants.DEFAULT_WEIGHT;
            if(validWeight(weight)) {
                serviceWeight = weight;
            }
            this.client.createEphemeralNode(CuratorUtils.getWeightPath(serviceName, serverAddress), String.valueOf(serviceWeight));
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: registerService failed to registerWeight serviceName[%s] serverAddress[%s]", serviceName, serverAddress));
            throw new RegisterException(e);
        }
        try {
            this.client.createEphemeralNode(CuratorUtils.getServicePath(serviceName, serverAddress));
            if(LOGGER.isDebugEnabled())
                LOGGER.debug(String.format("CuratorRegister: registerService success to registerService serviceName[%s] serverAddress[%s]", serviceName, serverAddress));
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: registerService failed to registerService serviceName[%s] serverAddress[%s]", serviceName, serverAddress));
            throw new RegisterException(e);
        }
    }

    @Override
    public void unregisterService(String serviceName, String serverAddress) throws RegisterException {
        if(StringUtils.isBlank(serviceName))
            throw new IllegalArgumentException("CuratorRegister: unregisterService param[serviceName] is null");
        if(StringUtils.isBlank(serverAddress))
            throw new IllegalArgumentException("CuratorRegister: unregisterService param[serverAddress] is null");
        String servicePath = CuratorUtils.getServicePath(serviceName, serverAddress);
        String weightPath = CuratorUtils.getWeightPath(serviceName, serverAddress);
        try {
            if(!client.exists(weightPath)) {
                if(LOGGER.isDebugEnabled())
                    LOGGER.debug(String.format("CuratorRegister: unregisterService not exist serviceNameWeight[%s] serverAddress[%s]", serviceName, serverAddress));
            } else {
                this.client.deleteNode(weightPath);
            }
            if(!client.exists((servicePath))) {
                if(LOGGER.isDebugEnabled())
                    LOGGER.debug(String.format("CuratorRegister: unregisterService not exist serviceName[%s] serverAddress[%s]", serviceName, serverAddress));
            } else {
                this.client.deleteNode(servicePath);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: unregisterService failed to unregister serviceName[%s] serverAddress[%s]", serviceName, serverAddress));
            throw new RegisterException(e);
        }
    }

    /**
     *
     * @param serviceName
     * @throws Exception

    private void clear(String serviceName) throws Exception {
        checkAndDelete(CuratorUtils.getRegisterServiceProviderRootPath(serviceName));
        checkAndDelete(CuratorUtils.getRegisterServiceWeightRootPath(serviceName));
        checkAndDelete(CuratorUtils.convertServicePath(serviceName));
    }

    private void checkAndDelete(String path) throws Exception {
        if (StringUtils.isBlank(path)) return;
        if (CollectionUtils.isEmpty(client.getChildrenNodes(path, false))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("check and delete node: path==> " + path);
            }
            client.deleteNode(path);
        }
    }
     */

    /**
     * 验证权重范围
     * @param weight
     * @return
     */
    private boolean validWeight(int weight) {
        return weight >= Constants.MIN_WEIGHT && weight <= Constants.MAX_WEIGHT;
    }

    /**
     * 获取服务权重
     * @param serviceName
     * @param serverAddress
     * @return
     */
    @Override
    public Integer getServiceWeight(String serviceName, String serverAddress) {
        if(StringUtils.isBlank(serviceName))
            throw new IllegalArgumentException("CuratorRegister: getServiceWeight param[serviceName] is null");
        if(StringUtils.isBlank(serverAddress))
            throw new IllegalArgumentException("CuratorRegister: getServiceWeight param[serverAddress] is null");
        String val = null;
        try {
            val = this.client.get(CuratorUtils.getWeightPath(serviceName, serverAddress), true);
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: getServiceWeight failed to get serviceName[%s], serverAddress[%s]", serviceName, serverAddress), e);
        }
        return StringUtils.isBlank(val) ? Constants.DEFAULT_WEIGHT : Integer.valueOf(val);
    }

    /**
     * 设置服务权重
     * @param serviceName
     * @param serverAddress
     * @param weight
     */
    @Override
    public void setServiceWeight(String serviceName, String serverAddress, int weight) {
        if(StringUtils.isBlank(serviceName))
            throw new IllegalArgumentException("CuratorRegister: setServiceWeight param[serviceName] is null");
        if(StringUtils.isBlank(serverAddress))
            throw new IllegalArgumentException("CuratorRegister: setServiceWeight param[serverAddress] is null");
        if(validWeight(weight)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("CuratorRegister: setServiceWeight param[weight] is invalid");
            }
            return;
        }
        String serverWeight = CuratorUtils.getWeightPath(serviceName, serverAddress);
        try {
            if(!client.exists(serverWeight)) {
                client.createEphemeralNode(serverWeight, String.valueOf(weight));
                return;
            }
            this.client.set(serverWeight, String.valueOf(weight));
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorRegister: setServiceWeight failed by serviceName[%s], serverAddress[%s]", serviceName, serverAddress), e);
        }
    }

    /**
     * 创建客户端
     * @throws InterruptedException
     */
    private void createClient() throws InterruptedException {
        if(properties == null) {
            throw new IllegalArgumentException("CuratorRegister: properties is null");
        }
        String address = properties.getProperty(ZOOKEEPER_ADDRESS_KEY);
        String namespace = properties.getProperty(ZOOKEEPER_SERVICES_KEY);
        if(StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("CuratorRegister: zookeeper address is null");
        }
        if(StringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("CuratorRegister: zookeeper namespace is null");
        }
        this.client = new CuratorClient(new CuratorParam(address, namespace,
                properties.getProperty(RETRY_TIME_LIMIT_KEY),
                properties.getProperty(RETRY_INTERVAL_KRY),
                properties.getProperty(SESSION_TIMEOUT_KEY),
                properties.getProperty(CONNECTION_TIMEOUT_KEY)));
    }
}
