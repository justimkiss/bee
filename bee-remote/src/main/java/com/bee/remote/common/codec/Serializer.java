package com.bee.remote.common.codec;

import com.bee.remote.common.codec.domain.DefaultResponse;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;
import org.apache.commons.lang.SerializationException;

/**
 * Created by jeoy.zhou on 12/11/15.
 */
public interface Serializer {

    public Object getProxy(InvokerConfig<?> invokerConfig);

    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException;

    public Object deserializeRequest(byte[] bytes) throws SerializationException;

    public DefaultResponse deserializeResponse(byte[] bytes) throws SerializationException;

    public byte[] serialize(Object body) throws SerializationException;

    public byte[] serializeRequest(Object request) throws SerializationException;

    public byte[] serializeResponse(Object response) throws SerializationException;

    public InvocationRequest newRequest(InvokerContext invokerContext) throws SerializationException;

    public InvocationResponse newResponse() throws SerializationException;
}
