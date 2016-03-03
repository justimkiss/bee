package com.bee.remote.netty.coder;

import com.bee.remote.netty.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public abstract class AbstractDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = Logger.getLogger(AbstractDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short parseClassValue = in.readShort();
        byte serializeType = in.readByte();
        Integer requestBodyLength = in.readInt();
        String parseClassName = NettyUtils.getClassName(new Integer(parseClassValue));
        if (StringUtils.isBlank(parseClassName))
            throw new IllegalArgumentException("parseClassName: " + parseClassName + ", not find");
        byte[] bytes = new byte[requestBodyLength];
        in.readBytes(bytes);
        Object request = deserialize(serializeType, bytes, parseClassName);
        out.add(request);
    }

    protected abstract Object deserialize(byte serializeType, byte[] bytes, String className) throws Exception;

}
