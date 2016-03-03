package com.bee.remote.netty.invoker.codec;

import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.common.codec.domain.InvocationSerializable;
import com.bee.remote.netty.coder.AbstractEncoder;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public class InvokerEncoder extends AbstractEncoder{

    private static final Logger LOGGER = Logger.getLogger(InvokerEncoder.class);

    public InvokerEncoder() {
        super();
    }

    @Override
    protected byte[] serialize(InvocationSerializable invocationSerializable) throws Exception {
        return SerializerFactory.getSerializer(invocationSerializable.getSerialize()).serializeRequest(invocationSerializable);
    }
}
