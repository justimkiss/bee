package com.bee.remote.invoker.callback;

import com.bee.remote.invoker.Client;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public interface Call {

    public void setClient(Client client);

    public Client getClient();
}
