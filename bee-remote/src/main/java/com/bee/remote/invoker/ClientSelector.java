package com.bee.remote.invoker;

import com.bee.common.extension.ExtensionLoader;
import com.bee.remote.invoker.domain.ConnectInfo;
import com.bee.remote.invoker.exception.ServiceUnavailableException;

import java.util.List;

/**
 * Created by jeoy.zhou on 2/16/16.
 */
public class ClientSelector {

    private static final List<ClientFactory> CLIENT_FACTORIES;

    static {
        CLIENT_FACTORIES = ExtensionLoader.getExtensionList(ClientFactory.class);
    }

    public static Client createClient(ConnectInfo connectInfo) {
        for(ClientFactory clientFactory : CLIENT_FACTORIES) {
            if(clientFactory.support(connectInfo))
                return clientFactory.createClient(connectInfo);
        }
        throw new ServiceUnavailableException("no available client been created from client factory:" + connectInfo);
    }
}
