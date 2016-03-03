package com.bee.remote.invoker;

import com.bee.remote.invoker.domain.ConnectInfo;

/**
 * Created by jeoy.zhou on 2/16/16.
 */
public interface ClientFactory {

    public boolean support(ConnectInfo connectInfo);

    public Client createClient(ConnectInfo connectInfo);
}
