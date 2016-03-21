package com.bee.register.listen.listener;

import java.util.List;

/**
 * Created by jeoy.zhou on 3/17/16.
 */
public interface ServiceChangeListener {

    /**
     * 当服务器地址变更
     * @param serviceName
     * @param hosts
     */
    public void onServiceAddressChange(String serviceName, List<String> hosts);

    /**
     * 服务器权重变更
     * @param host
     * @param weight
     */
    public void onWeightChange(String host, int weight);
}
