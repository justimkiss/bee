package com.bee.remote.common.codec.proto;

import com.bee.remote.common.codec.AbstractSerializer;
import com.bee.remote.common.codec.domain.DefaultRequest;
import com.bee.remote.common.codec.domain.DefaultResponse;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.collect.Maps;
import org.apache.commons.lang.SerializationException;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by jeoy.zhou on 12/11/15.
 */
public class ProtostuffSerializer extends AbstractSerializer{

    private final static ConcurrentMap<Class<?>, Schema<?>> CACHE_MAP = Maps.newConcurrentMap();

    static {
        registerClass(DefaultRequest.class);
        registerClass(DefaultResponse.class);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        try {
            T message = clazz.newInstance();
            Schema schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Throwable e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] serialize(Object obj) throws  SerializationException {
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        try {
            Schema schema = getSchema(obj.getClass());
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Throwable e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public DefaultRequest deserializeRequest(byte[] bytes) throws SerializationException {
        return deserialize(bytes, DefaultRequest.class);
    }

    @Override
    public DefaultResponse deserializeResponse(byte[] bytes) throws SerializationException {
        return deserialize(bytes, DefaultResponse.class);
    }

    @Override
    public byte[] serializeRequest(Object request) throws SerializationException {
        return serialize(request);
    }

    @Override
    public byte[] serializeResponse(Object response) throws SerializationException {
        return serialize(response);
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Object schemaObj = CACHE_MAP.get(clazz);
        if(schemaObj != null) return (Schema<T>) schemaObj;
        Schema<T> schema = RuntimeSchema.createFrom(clazz);
        CACHE_MAP.putIfAbsent(clazz, schema);
        return schema;
    }

    private static <T extends Object> void registerClass(Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.createFrom(clazz);
        boolean register = RuntimeSchema.register(clazz, schema);
        CACHE_MAP.putIfAbsent(clazz, schema);
    }

}
