package com.bee.remote.netty.invoker.codec;

import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.netty.coder.AbstractDecoder;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public class InvokerDecoder extends AbstractDecoder{

    private static final Logger LOGGER = Logger.getLogger(InvokerDecoder.class);

    public InvokerDecoder() {
        super();
    }

    @Override
    protected Object deserialize(byte serializeType, byte[] bytes, String className) throws Exception {
        return SerializerFactory.getSerializer(serializeType).deserialize(bytes, Class.forName(className));
    }
}
