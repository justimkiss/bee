package com.bee.remote.netty.provider.codec;

import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.netty.coder.AbstractDecoder;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public class ProviderDecoder extends AbstractDecoder {

    private static final Logger LOGGER = Logger.getLogger(ProviderDecoder.class);

    public ProviderDecoder() {
        super();
    }

    @Override
    protected Object deserialize(byte serializeType, byte[] bytes, String parseClassName) throws Exception {
        return SerializerFactory.getSerializer(serializeType).deserialize(bytes, Class.forName(parseClassName));
    }
}
