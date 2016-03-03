package com.bee.config.utills;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jeoy.zhou on 12/7/15.
 */
public class PropertiesUtils {

    private static final Logger LOGGER = Logger.getLogger(PropertiesUtils.class);
    private static final String COMMENT_PREFIX = "#";

    public static void convertPropertiesToMap(Map<String, String> results, File file) {
        if(file == null || !file.exists()) {
            LOGGER.error("params:[file] is empty or not exists");
            return;
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
           LOGGER.error("read file error", e);
            return;
        }
        convertPropertiesToMap(results, properties);
    }

    public static void convertPropertiesToMap(Map<String, String> results, Properties properties) {
        if(results == null) {
            LOGGER.error("params:[results] is null");
            return;
        }
        if(MapUtils.isEmpty(properties)) {
            LOGGER.error(("params:[properties] is empty"));
            return;
        }
        Iterator<?> iterator = properties.keySet().iterator();
        String key = null;
        String value = null;
        while(iterator.hasNext()) {
            key = iterator.next().toString();
            if(key.startsWith(COMMENT_PREFIX)) {
                // 注释的属性不加载
                continue;
            }
            value = properties.getProperty(key);
            if(StringUtils.isBlank(value)) {
                LOGGER.error(String.format("properties file has valid value, key[%s]", key));
                continue;
            }
            results.put(key, value);
        }
    }
}
