package com.bee.remote.common.codec;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.proto.ProtostuffSerializer;
import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/11/15.
 */
public final class SerializerFactory {

    private static final Logger LOGGER = Logger.getLogger(SerializerFactory.class);
    private static final ConcurrentHashMap<Byte, Serializer> SERIALIZER_MAP = new ConcurrentHashMap();
    private static boolean isInit = false;


    static {
        init();
    }

    /**
     * 初始化序列化集合
     */
    public static void init() {
        if(!isInit) {
            registerSerializer(Constants.SERIALIZER_PROTO, new ProtostuffSerializer());
            isInit = true;
        }
    }

    public static Serializer getSerializer(Byte key) {
        Serializer serializer = SERIALIZER_MAP.get(key);
        if(serializer == null) {
            throw new InvalidParameterException("SerializerFactory: getSerializer return null, by key: " + key);
        }
        return serializer;
    }

    public static byte getSerialize(String serialize) {
        if (Constants.PROTO.equalsIgnoreCase(serialize)) {
            return Constants.SERIALIZER_PROTO;
        }
        throw new IllegalArgumentException("no serialize: " + serialize);
    }

    /**
     * 注册序列化
     * @param serializerType
     * @param serializer
     */
    public static void registerSerializer(Byte serializerType, Serializer serializer) {
        if(serializerType == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("SerializerFactory: registerSerializer param[serializerType] is null");
                return;
            }
        }
        if(serializer == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("SerializerFactory: registerSerializer param[serializer] is null");
                return;
            }
        }
        SERIALIZER_MAP.putIfAbsent(serializerType, serializer);
    }


}
