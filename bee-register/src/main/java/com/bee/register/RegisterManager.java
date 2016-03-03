package com.bee.register;

import com.bee.common.constants.Constants;
import com.bee.common.extension.ExtensionLoader;
import com.bee.register.config.DefaultRegisterConfigManager;
import com.bee.register.config.RegisterConfigManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

/**
 * Created by jeoy.zhou on 12/18/15.
 */
public class RegisterManager {

    private static final Logger LOGGER = Logger.getLogger(RegisterManager.class);
    private static RegisterManager registerManager = new RegisterManager();
    private static final Register REGISTER = ExtensionLoader.getExtension(Register.class);
    private static final RegisterConfigManager REGISTER_CONFIG_MANAGER = new DefaultRegisterConfigManager();
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
    }

    /**
     * 注销服务
     * @param serviceName
     * @param serverAddress
     */
    public void unregisterService(String serviceName, String serverAddress) {
        REGISTER.unregisterService(serviceName, serverAddress);
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        RegisterManager.properties = properties;
    }
}
