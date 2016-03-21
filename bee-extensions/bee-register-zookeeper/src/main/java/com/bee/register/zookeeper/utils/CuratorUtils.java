package com.bee.register.zookeeper.utils;


import com.bee.common.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/23/15.
 */
public class CuratorUtils {

    private static final Logger logger = Logger.getLogger(CuratorUtils.class);

    /**
     * 注册服务path
     * @param serviceName
     * @param serviceAddress
     * @return
     */
    public static String getServicePath(String serviceName, String serviceAddress) {
        return getRegisterServiceProviderRootPath(serviceName) + CuratorUtils.convertServicePath(serviceAddress);
    }

    /**
     * 注册消费节点path
     * @param serviceName
     * @param serviceAddress
     * @return
     */
    public static String getConsumerPath(String serviceName, String serviceAddress) {
        return getRegisterServiceConsumerRootPath(serviceName) + CuratorUtils.convertServicePath(serviceAddress);
    }

    /**
     * 注册服务权重path
     * @param serviceName
     * @param serviceAddress
     * @return
     */
    public static String getWeightPath(String serviceName, String serviceAddress) {
        return getRegisterServiceWeightRootPath(serviceName) + CuratorUtils.convertServicePath(serviceAddress);
    }

    /**
     * 注册服务path根节点
     * /bee/server/${serviceName}/providers
     * @param serviceName
     * @return
     */
    public static String getRegisterServiceProviderRootPath(String serviceName) {
        return CuratorUtils.convertServicePath(serviceName) +
                Constants.PATH_SEPARATOR + Constants.ZOOKEEPER_PROVIDERS_NODE;
    }

    /**
     * 注册消费path根节点
     * /bee/server/${serviceName}/consumers
     * @param serviceName
     * @return
     */
    public static String getRegisterServiceConsumerRootPath(String serviceName) {
        return CuratorUtils.convertServicePath(serviceName) +
                Constants.PATH_SEPARATOR + Constants.ZOOKEEPER_CONSUMER_NODE;
    }

    /**
     * service权重path根节点
     * /bee/server/${serviceName}/weights
     * @param serviceName
     * @return
     */
    public static String getRegisterServiceWeightRootPath(String serviceName) {
        return CuratorUtils.convertServicePath(serviceName) +
                Constants.PATH_SEPARATOR + Constants.ZOOKEEPER_WEIGHT_NODE;
    }

    /**
     * 验证是否是合法的serviceaddress
     * @param address
     * @return
     */
    public static boolean isValidAddress(String address) {
        if(StringUtils.isBlank(address)
                || address.indexOf(":") == -1
                || address.length() < 10) {
            return false;
        }
        return true;
    }

    public static String convertServicePath(String servicePath) {
        if(servicePath.startsWith(Constants.PATH_SEPARATOR)) {
            return Constants.PATH_SEPARATOR + escape(servicePath.substring(1));
        }
        return Constants.PATH_SEPARATOR + escape(servicePath);
    }

    /**
     * 将servicepath中的“/"转换为“@”
     * @param path
     * @return
     */
    public static String escape(String path) {
        return path.replaceAll(Constants.PATH_SEPARATOR, Constants.PLACEHOLDER);
    }

    /**
     * 将servicepath中的“@”转换为“/”
     * @param path
     * @return
     */
    public static String unescape(String path) {
        return path.replaceAll(Constants.PLACEHOLDER, Constants.PATH_SEPARATOR);
    }
}
