package com.bee.remote.netty.invoker;

import com.bee.common.constants.Constants;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.ClientFactory;
import com.bee.remote.invoker.domain.ConnectInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by jeoy.zhou on 2/16/16.
 */
public class NettyClientFactory implements ClientFactory{

    @Override
    public boolean support(ConnectInfo connectInfo) {
        Map<String, Integer> serviceNames = connectInfo.getServiceNames();
        if (MapUtils.isNotEmpty(serviceNames)) {
            String serviceName = serviceNames.keySet().iterator().next();
            if(StringUtils.isNotBlank(serviceName) && serviceName.startsWith(Constants.PLACEHOLDER)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Client createClient(ConnectInfo connectInfo) {
        return new NettyClient(connectInfo);
    }
}
