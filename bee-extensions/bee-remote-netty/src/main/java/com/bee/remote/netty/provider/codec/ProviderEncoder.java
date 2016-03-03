package com.bee.remote.netty.provider.codec;

import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.common.codec.domain.InvocationSerializable;
import com.bee.remote.netty.coder.AbstractEncoder;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public class ProviderEncoder extends AbstractEncoder{

    private static final Logger LOGGER = Logger.getLogger(ProviderEncoder.class);

    public ProviderEncoder() {
        super();
    }

    @Override
    protected byte[] serialize(InvocationSerializable invocationSerializable) throws Exception {
        return SerializerFactory.getSerializer(invocationSerializable.getSerialize()).serialize(invocationSerializable);
    }
}
