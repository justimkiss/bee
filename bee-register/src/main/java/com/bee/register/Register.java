package com.bee.register;

import com.bee.common.exception.RegisterException;

import java.util.List;
import java.util.Properties;

/**
 * Created by jeoy.zhou on 12/18/15.
 */
public interface Register {

    /**
     * 初始化
     */
    public void init(Properties properties);

    /**
     * 注册器名称
     * @return
     */
    public String name();

    /**
     * 获取service的provider地址
     * @param serviceName
     * @return
     */
    public List<String> getServiceAddressList(String serviceName);

    /**
     * 注册provider服务地址
     * @param serviceName
     * @param serverAddress
     * @param weight
     * @throws RegisterException
     */
    public void registerService(String serviceName, String serverAddress, int weight) throws RegisterException;

    /**
     * 注销provider服务地址
     * @param serviceName
     * @param serverAddress
     * @throws RegisterException
     */
    public void unregisterService(String serviceName, String serverAddress) throws RegisterException;

    /**
     * 获取服务权重
     * @param serviceName
     * @return
     */
    public Integer getServiceWeight(String serviceName, String weight);

    /**
     * 设置服务权重
     * @param serviceName
     * @param serverAddress
     * @param weight
     */
    public void setServiceWeight(String serviceName, String serverAddress, int weight);


}
