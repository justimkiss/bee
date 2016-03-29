package com.bee.register.config;

import com.bee.common.constants.Constants;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Properties;

/**
 * Created by jeoy.zhou on 3/3/16.
 */
public class DefaultRegisterConfigManager implements RegisterConfigManager {

    private static final Logger LOGGER = Logger.getLogger(DefaultRegisterConfigManager.class);

    private static final String ZOOKEEPER_CONFIG_PATH = Constants.ZOOKEEPER_CONFIG_PATH;
    private static final String GLOBAL_ZOOKEEPER_CONFIG_PATH = Constants.GLOBAL_ZOOKEEPER_CONFIG_PATH;

    @Override
    public Properties getRegisterConfig() {
        File file = null;
        try {
            file = ResourceUtils.getFile(ZOOKEEPER_CONFIG_PATH);
        } catch (FileNotFoundException e) {
            LOGGER.warn(String.format("file not found, path:[%s]", ZOOKEEPER_CONFIG_PATH));
            file = new File(GLOBAL_ZOOKEEPER_CONFIG_PATH);
            if(!file.exists() || !file.isFile()) {
                LOGGER.warn(String.format("file not found, path:[%s]", GLOBAL_ZOOKEEPER_CONFIG_PATH));
                throw new IllegalArgumentException("not find resource: " + GLOBAL_ZOOKEEPER_CONFIG_PATH);
            }
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("load resource error: ", e);
        }
        return properties;
    }

}

