package com.bee.remote.netty.coder;

import com.bee.remote.common.codec.domain.InvocationSerializable;
import com.bee.remote.netty.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public abstract class AbstractEncoder extends MessageToByteEncoder<InvocationSerializable> {

    private static final Logger LOGGER = Logger.getLogger(AbstractEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, InvocationSerializable invocationSerializable, ByteBuf out) throws Exception {
        String requestClass = invocationSerializable.getClass().getName();
        Integer requestClassValue = NettyUtils.getTypeValue(requestClass);
        if (requestClassValue == null)
            throw new IllegalArgumentException("requestClass:" + requestClass + " not register");
        byte[] body = _encode(ctx, invocationSerializable, out);
        out.writeInt(body.length);
        out.writeShort(requestClassValue);
        out.writeByte(invocationSerializable.getSerialize());
        out.writeBytes(body);
    }

    protected byte[] _encode(ChannelHandlerContext ctx, InvocationSerializable invocationSerializable, ByteBuf out) throws Exception {
        beforeSerialize(invocationSerializable);
        byte[] body = serialize(invocationSerializable);
        afterSerialized(invocationSerializable);
        return body;
    }

    protected void beforeSerialize(InvocationSerializable invocationSerializable) {

    }

    protected void afterSerialized(InvocationSerializable invocationSerializable) {

    }


    protected abstract byte[] serialize(InvocationSerializable invocationSerializable) throws Exception;
}
