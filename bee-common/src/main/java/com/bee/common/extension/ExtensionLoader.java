package com.bee.common.extension;

import com.bee.common.extension.entity.ExtensionNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/5/15.
 */
public final class ExtensionLoader {

    private static Map<Class<?>, Object> EXTENSION_MAP= new ConcurrentHashMap<Class<?>, Object>();

    private static Map<Class<?>, List<?>> EXTENSION_LIST_MAP = new ConcurrentHashMap<Class<?>, List<?>>();

    public static <T> T newExtension(Class<T> clz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clz);
        for(T t : serviceLoader) {
            return t;
        }
        return null;
    }

    public static <T> List<T> newExtensionList(Class<T> clz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clz);
        List<T> ts = new ArrayList<T>();
        for(T t : serviceLoader) {
            ts.add(t);
        }
        return ts;
    }

    public static <T> List<T> getExtensionList(Class<T> clz) {
        List<T> tList = (List<T>) EXTENSION_LIST_MAP.get(clz);
        if(tList == null) {
            tList = ExtensionLoader.newExtensionList(clz);
            if(tList.isEmpty()) {
                EXTENSION_LIST_MAP.put(clz, ExtensionNull.LIST_EMPTY);
            } else {
                EXTENSION_LIST_MAP.put(clz, ExtensionNull.LIST_EMPTY);
            }
        } else if(tList == ExtensionNull.LIST_EMPTY) {
            tList = null;
        }
        return tList;
    }

    public static <T> T getExtension(Class<T> clz) {
        T t = (T) EXTENSION_MAP.get(clz);
        if(t == null) {
            t = ExtensionLoader.newExtension(clz);
            if( t == null) {
                EXTENSION_MAP.put(clz, ExtensionNull.EMPTY);
            } else {
                EXTENSION_MAP.put(clz, t);
            }
        } else if(t == ExtensionNull.EMPTY) {
            t = null;
        }
        return t;
    }
}

