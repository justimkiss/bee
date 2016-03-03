package com.bee.remote.invoker.listener;

import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.domain.ConnectInfo;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public interface ClusterListener {

    public void addConnected(ConnectInfo connectInfo);

    public void removeConnected(Client client);
}
