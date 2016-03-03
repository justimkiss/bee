package com.bee.remote.provider.domain;

import com.bee.remote.common.codec.domain.InvocationResponse;

/**
 * Created by jeoy.zhou on 3/1/16.
 */
public interface ProviderChannel {

    public String getRemoteAddress();

    public void write(InvocationResponse response);

}
