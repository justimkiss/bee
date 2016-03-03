package com.bee.remote.netty.utils;

import com.bee.remote.common.codec.domain.DefaultRequest;
import com.bee.remote.common.codec.domain.DefaultResponse;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeoy.zhou on 2/22/16.
 */
public final class NettyUtils {

    private static final Logger LOGGER = Logger.getLogger(NettyUtils.class);

    private static final Map<String, Integer> TYPE_MAP = new HashMap<String, Integer>();
    private static final Map<Integer, String> CONVERT_TYPE_MAP = new HashMap<Integer, String>();

    static {
        register(DefaultRequest.class.getName(), 10);
        register(DefaultResponse.class.getName(), 20);
    }

    public static final String getClassName(Integer typeValue) {
        return CONVERT_TYPE_MAP.get(typeValue);
    }

    public static final Integer getTypeValue(String className) {
        return TYPE_MAP.get(className);
    }

    private static synchronized final void register(String typeName, Integer typeValue) {
        TYPE_MAP.put(typeName, typeValue);
        CONVERT_TYPE_MAP.put(typeValue, typeName);
    }

    private static synchronized final void cancel(String typeName) {
        Integer typeValue = TYPE_MAP.remove(typeName);
        CONVERT_TYPE_MAP.remove(typeValue);
    }

    private static synchronized final void cancel(Integer typeValue) {
        String typeName= CONVERT_TYPE_MAP.remove(typeValue);
        TYPE_MAP.remove(typeName);
    }
}
